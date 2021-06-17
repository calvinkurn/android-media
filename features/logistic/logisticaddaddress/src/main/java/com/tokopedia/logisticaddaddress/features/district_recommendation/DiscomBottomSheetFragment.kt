package com.tokopedia.logisticaddaddress.features.district_recommendation

import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.beloo.widget.chipslayoutmanager.ChipsLayoutManager
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.recyclerview.EndlessRecyclerViewScrollListener
import com.tokopedia.design.component.BottomSheets
import com.tokopedia.kotlin.extensions.view.getScreenHeight
import com.tokopedia.logisticCommon.data.entity.response.Data
import com.tokopedia.logisticaddaddress.R
import com.tokopedia.logisticaddaddress.common.AddressConstants.*
import com.tokopedia.logisticaddaddress.databinding.BottomsheetDistrictRecommendationBinding
import com.tokopedia.logisticaddaddress.di.DaggerDistrictRecommendationComponent
import com.tokopedia.logisticaddaddress.domain.model.Address
import com.tokopedia.logisticaddaddress.features.addnewaddress.ChipsItemDecoration
import com.tokopedia.logisticaddaddress.features.addnewaddress.addedit.ZipCodeChipsAdapter
import com.tokopedia.logisticaddaddress.features.addnewaddress.analytics.AddNewAddressAnalytics
import com.tokopedia.logisticaddaddress.features.district_recommendation.adapter.DiscomAdapterRevamp
import com.tokopedia.logisticaddaddress.features.district_recommendation.adapter.DiscomNewAdapter
import com.tokopedia.logisticaddaddress.features.district_recommendation.adapter.PopularCityAdapter
import com.tokopedia.network.utils.ErrorHandler
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.utils.lifecycle.autoCleared
import kotlinx.android.synthetic.main.form_add_new_address_mismatch_data_item.*
import rx.Emitter
import rx.Observable
import rx.Subscription
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import rx.subscriptions.CompositeSubscription
import java.util.concurrent.TimeUnit
import javax.inject.Inject

/**
 * Created by fwidjaja on 2019-05-29.
 */
class DiscomBottomSheetFragment : BottomSheets(),
        PopularCityAdapter.ActionListener,
        DiscomContract.View,
        DiscomNewAdapter.ActionListener,
        DiscomAdapterRevamp.ActionListener,
        ZipCodeChipsAdapter.ActionListener {

    private lateinit var popularCityAdapter: PopularCityAdapter
    private lateinit var listDistrictAdapter: DiscomNewAdapter
    private lateinit var listDistrictAdapterRevamp: DiscomAdapterRevamp
    private lateinit var zipCodeChipsAdapter: ZipCodeChipsAdapter
    private lateinit var chipsLayoutManagerZipCode: ChipsLayoutManager
    private var isLoading: Boolean = false
    private var input: String = ""
    private val mLayoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
    private val mEndlessListener = object : EndlessRecyclerViewScrollListener(mLayoutManager) {
        override fun onLoadMore(page: Int, totalItemsCount: Int) {
            presenter.loadData(input, page + 1)
        }
    }
    private var mIsInitialLoading: Boolean = false
    private val mCompositeSubs: CompositeSubscription = CompositeSubscription()
    val handler = Handler()
    private lateinit var actionListener: ActionListener
    private var isFullFlow: Boolean = true
    private var isLogisticLabel: Boolean = true
    private var isAnaRevamp: Boolean = true
    private var staticDimen8dp: Int? = 0

    private var binding by autoCleared<BottomsheetDistrictRecommendationBinding>()

    @Inject
    lateinit var presenter: DiscomContract.Presenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            isFullFlow = it.getBoolean(EXTRA_IS_FULL_FLOW, true)
            isLogisticLabel = it.getBoolean(EXTRA_IS_LOGISTIC_LABEL, true)
            isAnaRevamp = it.getBoolean(EXTRA_IS_ANA_REVAMP, true)
        }
    }

    override fun getLayoutResourceId(): Int {
        return R.layout.bottomsheet_district_recommendation
    }

    override fun initView(view: View) {
        binding = BottomsheetDistrictRecommendationBinding.bind(view)
        prepareLayout(view)
        initInjector()
        setViewListener()
    }

    private fun prepareLayout(view: View) {
        binding.llListDistrict.visibility = View.GONE

        val cityList = resources.getStringArray(R.array.cityList)
        val chipsLayoutManager = ChipsLayoutManager.newBuilder(view.context)
                .setOrientation(ChipsLayoutManager.HORIZONTAL)
                .setRowStrategy(ChipsLayoutManager.STRATEGY_DEFAULT)
                .build()

        zipCodeChipsAdapter = ZipCodeChipsAdapter(context, this)

        chipsLayoutManagerZipCode = ChipsLayoutManager.newBuilder(view.context)
                .setOrientation(ChipsLayoutManager.HORIZONTAL)
                .setRowStrategy(ChipsLayoutManager.STRATEGY_DEFAULT)
                .setScrollingEnabled(true)
                .build()

        ViewCompat.setLayoutDirection(binding.rvChips, ViewCompat.LAYOUT_DIRECTION_LTR)
        popularCityAdapter = PopularCityAdapter(context, this)
        popularCityAdapter.cityList = cityList.toMutableList()

        binding.rvChips.apply {
            val dist = context.resources.getDimensionPixelOffset(com.tokopedia.unifyprinciples.R.dimen.unify_space_8)
            layoutManager = chipsLayoutManager
            adapter = popularCityAdapter
            addItemDecoration(ChipsItemDecoration(dist))
        }

        listDistrictAdapter = DiscomNewAdapter(this)
        listDistrictAdapterRevamp = DiscomAdapterRevamp(this)

        if (isAnaRevamp) {
            binding.rvListDistrict.apply {
                layoutManager = mLayoutManager
                adapter = listDistrictAdapterRevamp
            }
        } else {
            binding.rvListDistrict.apply {
                layoutManager = mLayoutManager
                adapter = listDistrictAdapter
            }
        }

        binding.etSearchDistrictRecommendation.setSelection(binding.etSearchDistrictRecommendation.text.length)
    }

    override fun onDetach() {
        super.onDetach()
        presenter.detach()
        mCompositeSubs.unsubscribe()
    }

    override fun title(): String {
        return getString(R.string.kota_kecamatan)
    }

    override fun state(): BottomSheetsState {
        return BottomSheetsState.FULL
    }

    override fun configView(parentView: View?) {
        super.configView(parentView)
        parentView?.findViewById<View>(com.tokopedia.purchase_platform.common.R.id.layout_title)?.setOnClickListener(null)
        parentView?.findViewById<View>(com.tokopedia.purchase_platform.common.R.id.btn_close)?.setOnClickListener {
            AddNewAddressAnalytics.eventClickBackArrowOnNegativePage(isFullFlow, isLogisticLabel)
            onCloseButtonClick()
        }
    }

    override fun renderData(list: List<Address>, hasNextPage: Boolean) {
        isLoading = false
        binding.llPopularCity.visibility = View.GONE
        binding.llListDistrict.visibility = View.VISIBLE
        binding.tvDescInputDistrict.visibility = View.VISIBLE
        binding.tvDescInputDistrict.setText(R.string.hint_advice_search_address)
        if (mIsInitialLoading) {
            if (isAnaRevamp) listDistrictAdapterRevamp.setData(list)
            else listDistrictAdapter.setData(list)
            mEndlessListener.resetState()
            mIsInitialLoading = false
        } else {
            if (isAnaRevamp) listDistrictAdapterRevamp.appendData(list)
            else listDistrictAdapter.appendData(list)
            mEndlessListener.updateStateAfterGetData()
        }
        mEndlessListener.setHasNextPage(hasNextPage)
    }

    override fun showGetListError(throwable: Throwable) {
        val message = ErrorHandler.getErrorMessage(context, throwable)
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    override fun setLoadingState(active: Boolean) {
        if (active) binding.icClose.visibility = View.INVISIBLE
        else {
            binding.icClose.visibility = View.VISIBLE
            binding.icClose.setOnClickListener {
                binding.etSearchDistrictRecommendation.setText("")
                binding.llListDistrict.visibility = View.GONE
                binding.llPopularCity.visibility = View.VISIBLE
                popularCityAdapter.notifyDataSetChanged()
                binding.icClose.visibility = View.GONE
            }
        }
        binding.progressBar.visibility = if (active) View.VISIBLE else View.INVISIBLE
    }

    override fun showEmpty() {
        binding.tvDescInputDistrict.visibility = View.VISIBLE
        binding.tvDescInputDistrict.setText(R.string.hint_search_address_no_result)
        binding.llPopularCity.visibility = View.VISIBLE
        binding.llListDistrict.visibility = View.GONE
    }

    override fun setResultDistrict(data: Data, lat: Double, long: Double) {
        // no op
    }

    override fun onCityChipClicked(city: String) {
        binding.etSearchDistrictRecommendation.setText(city)
        binding.etSearchDistrictRecommendation.setSelection(city.length)
        AddNewAddressAnalytics.eventClickChipsKotaKecamatanChangeAddressNegative(isFullFlow, isLogisticLabel)
    }

    override fun showToasterError() {
        // no-op
    }

    fun setActionListener(actionListener: ActionListener) {
        this.actionListener = actionListener
    }

    private fun initInjector() {
        DaggerDistrictRecommendationComponent.builder()
                .baseAppComponent((context?.applicationContext as BaseMainApplication).baseAppComponent)
                .build().inject(this)
        presenter.attach(this)
    }

    private fun setViewListener() {
        binding.etSearchDistrictRecommendation.apply {
            isFocusableInTouchMode = true
            requestFocus()
        }
        watchTextRx(binding.etSearchDistrictRecommendation)
                .subscribe { s ->
                    if (s.isNotEmpty()) {
                        input = s
                        mIsInitialLoading = true
                        handler.postDelayed({
                            presenter.loadData(input, 1)
                        }, 200)
                    } else {
                        binding.icClose.visibility = View.GONE
                        binding.tvDescInputDistrict.visibility = View.GONE
                        binding.llPopularCity.visibility = View.VISIBLE
                        binding.llListDistrict.visibility = View.GONE
                    }
                }.toCompositeSubs()

        binding.rvListDistrict.addOnScrollListener(mEndlessListener)
    }

    override fun onDistrictItemClicked(districtModel: Address) {
        context?.let {
            districtModel.run {
                actionListener.onGetDistrict(districtModel)
                AddNewAddressAnalytics.eventClickSuggestionKotaKecamatanChangeAddressNegative(isFullFlow, isLogisticLabel)
                dismiss()
            }
        }
    }

    override fun onDistrictItemRevampClicked(districtModel: Address) {
        context?.let {
            districtModel.run {
                actionListener.onGetDistrict(districtModel)
                AddNewAddressAnalytics.eventClickSuggestionKotaKecamatanChangeAddressNegative(isFullFlow, isLogisticLabel)
                setupRvZipCodeChips()
                getDistrict(districtModel)
            }
        }
    }

    private fun watchTextRx(view: EditText): Observable<String> {
        return Observable
                .create({ emitter: Emitter<String> ->
                    view.addTextChangedListener(object : TextWatcher {
                        override fun afterTextChanged(editable: Editable?) {
                            emitter.onNext(editable.toString())
                        }

                        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

                        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
                    })
                }, Emitter.BackpressureMode.NONE)
                .filter { t -> t.isEmpty() || t.length > 2 }
                .debounce(700, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
    }

    private fun Subscription.toCompositeSubs() {
        mCompositeSubs.add(this)
    }

    fun getDistrict(data: Address) {
        binding.llZipCode.visibility = View.VISIBLE
        binding.cardAddress.addressDistrict.text = "${data?.districtName}, ${data?.cityName}, ${data?.provinceName}"
        binding.etKodepos.textFieldInput.apply {
            setOnClickListener {
                showZipCodes(data)
            }
        }
    }

    private fun setupRvZipCodeChips() {
        binding.rvKodeposChips.apply {
            staticDimen8dp?.let { ChipsItemDecoration(it) }?.let { addItemDecoration(it) }
            layoutManager = chipsLayoutManagerZipCode
            adapter = zipCodeChipsAdapter
        }
    }

    private fun showZipCodes(data: Address) {
        ViewCompat.setLayoutDirection(binding.rvKodeposChips, ViewCompat.LAYOUT_DIRECTION_LTR)
        data.zipCodes?.let {
            binding.rvKodeposChips.visibility = View.VISIBLE
            zipCodeChipsAdapter.zipCodes = it.toMutableList()
            zipCodeChipsAdapter.notifyDataSetChanged()
        }
    }

    interface ActionListener {
        fun onGetDistrict(districtAddress: Address)
    }

    companion object {

        private const val MAX_HEIGHT_MULTIPLIER = 0.90

        @JvmStatic
        fun newInstance(isLogisticLabel: Boolean, isAnaRevamp: Boolean): DiscomBottomSheetFragment {
            return DiscomBottomSheetFragment().apply {
                arguments = Bundle().apply {
                    putBoolean(EXTRA_IS_LOGISTIC_LABEL, isLogisticLabel)
                    putBoolean(EXTRA_IS_ANA_REVAMP, isAnaRevamp)
                }
            }
        }
    }

    override fun onZipCodeClicked(zipCode: String) {
        binding.rvKodeposChips.visibility = View.GONE
        binding.etKodepos.textFieldInput.run {
            setText(zipCode)
        }
    }

}
