package com.tokopedia.logisticaddaddress.features.district_recommendation

import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.*
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.beloo.widget.chipslayoutmanager.ChipsLayoutManager
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.recyclerview.EndlessRecyclerViewScrollListener
import com.tokopedia.design.component.BottomSheets
import com.tokopedia.logisticCommon.data.entity.response.Data
import com.tokopedia.logisticaddaddress.R
import com.tokopedia.logisticaddaddress.common.AddressConstants.EXTRA_IS_FULL_FLOW
import com.tokopedia.logisticaddaddress.common.AddressConstants.EXTRA_IS_LOGISTIC_LABEL
import com.tokopedia.logisticaddaddress.di.DaggerDistrictRecommendationComponent
import com.tokopedia.logisticaddaddress.domain.model.Address
import com.tokopedia.logisticaddaddress.features.addnewaddress.ChipsItemDecoration
import com.tokopedia.logisticaddaddress.features.addnewaddress.analytics.AddNewAddressAnalytics
import com.tokopedia.logisticaddaddress.features.district_recommendation.adapter.DiscomNewAdapter
import com.tokopedia.logisticaddaddress.features.district_recommendation.adapter.PopularCityAdapter
import com.tokopedia.network.utils.ErrorHandler
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
        DiscomNewAdapter.ActionListener {

    private var bottomSheetView: View? = null
    private lateinit var popularCityAdapter: PopularCityAdapter
    private lateinit var listDistrictAdapter: DiscomNewAdapter
    private lateinit var rvChips: RecyclerView
    private lateinit var etSearch: EditText
    private lateinit var llListDistrict: LinearLayout
    private lateinit var llPopularCity: LinearLayout
    private lateinit var rvListDistrict: RecyclerView
    private lateinit var icCloseBtn: ImageView
    private lateinit var mProgressbar: ProgressBar
    private lateinit var mMessage: TextView
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

    @Inject
    lateinit var presenter: DiscomContract.Presenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            isFullFlow = it.getBoolean(EXTRA_IS_FULL_FLOW, true)
            isLogisticLabel = it.getBoolean(EXTRA_IS_LOGISTIC_LABEL, true)
        }
    }

    override fun getLayoutResourceId(): Int {
        return R.layout.bottomsheet_district_recommendation
    }

    override fun initView(view: View) {
        prepareLayout(view)
        initInjector()
        setViewListener()
    }

    private fun prepareLayout(view: View) {
        bottomSheetView = view
        rvChips = view.findViewById(R.id.rv_chips)
        etSearch = view.findViewById(R.id.et_search_district_recommendation)
        llPopularCity = view.findViewById(R.id.ll_popular_city)
        rvListDistrict = view.findViewById(R.id.rv_list_district)
        icCloseBtn = view.findViewById(R.id.ic_close)
        mProgressbar = view.findViewById(R.id.progress_bar)
        llListDistrict = view.findViewById(R.id.ll_list_district)
        mMessage = view.findViewById(R.id.tv_desc_input_district)
        llListDistrict.visibility = View.GONE

        val cityList = resources.getStringArray(R.array.cityList)
        val chipsLayoutManager = ChipsLayoutManager.newBuilder(view.context)
                .setOrientation(ChipsLayoutManager.HORIZONTAL)
                .setRowStrategy(ChipsLayoutManager.STRATEGY_DEFAULT)
                .build()

        ViewCompat.setLayoutDirection(rvChips, ViewCompat.LAYOUT_DIRECTION_LTR)
        popularCityAdapter = PopularCityAdapter(context, this)
        popularCityAdapter.cityList = cityList.toMutableList()

        rvChips.apply {
            val dist = context.resources.getDimensionPixelOffset(com.tokopedia.unifyprinciples.R.dimen.unify_space_8)
            layoutManager = chipsLayoutManager
            adapter = popularCityAdapter
            addItemDecoration(ChipsItemDecoration(dist))
        }

        listDistrictAdapter = DiscomNewAdapter(this)
        rvListDistrict.apply {
            layoutManager = mLayoutManager
            adapter = listDistrictAdapter
        }

        etSearch.setSelection(etSearch.text.length)
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
        llPopularCity.visibility = View.GONE
        llListDistrict.visibility = View.VISIBLE
        mMessage.visibility = View.VISIBLE
        mMessage.setText(R.string.hint_advice_search_address)
        if (mIsInitialLoading) {
            listDistrictAdapter.setData(list)
            mEndlessListener.resetState()
            mIsInitialLoading = false
        } else {
            listDistrictAdapter.appendData(list)
            mEndlessListener.updateStateAfterGetData()
        }
        mEndlessListener.setHasNextPage(hasNextPage)
    }

    override fun showGetListError(throwable: Throwable) {
        val message = ErrorHandler.getErrorMessage(context, throwable)
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    override fun setLoadingState(active: Boolean) {
        if (active) icCloseBtn.visibility = View.INVISIBLE
        else {
            icCloseBtn.visibility = View.VISIBLE
            icCloseBtn.setOnClickListener {
                etSearch.setText("")
                llListDistrict.visibility = View.GONE
                llPopularCity.visibility = View.VISIBLE
                popularCityAdapter.notifyDataSetChanged()
                icCloseBtn.visibility = View.GONE
            }
        }
        mProgressbar.visibility = if (active) View.VISIBLE else View.INVISIBLE
    }

    override fun showEmpty() {
        mMessage.visibility = View.VISIBLE
        mMessage.setText(R.string.hint_search_address_no_result)
        llPopularCity.visibility = View.VISIBLE
        llListDistrict.visibility = View.GONE
    }

    override fun setResultDistrict(data: Data, lat: Double, long: Double) {
        // no op
    }

    override fun onCityChipClicked(city: String) {
        etSearch.setText(city)
        etSearch.setSelection(city.length)
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
        etSearch.apply {
            isFocusableInTouchMode = true
            requestFocus()
        }
        watchTextRx(etSearch)
                .subscribe { s ->
                    if (s.isNotEmpty()) {
                        input = s
                        mIsInitialLoading = true
                        handler.postDelayed({
                            presenter.loadData(input, 1)
                        }, 200)
                    } else {
                        icCloseBtn.visibility = View.GONE
                        mMessage.visibility = View.GONE
                        llPopularCity.visibility = View.VISIBLE
                        llListDistrict.visibility = View.GONE
                    }
                }.toCompositeSubs()

        rvListDistrict.addOnScrollListener(mEndlessListener)
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

    interface ActionListener {
        fun onGetDistrict(districtAddress: Address)
    }

    companion object {
        @JvmStatic
        fun newInstance(isLogisticLabel: Boolean): DiscomBottomSheetFragment {
            return DiscomBottomSheetFragment().apply {
                arguments = Bundle().apply {
                    putBoolean(EXTRA_IS_LOGISTIC_LABEL, isLogisticLabel)
                }
            }
        }
    }
}
