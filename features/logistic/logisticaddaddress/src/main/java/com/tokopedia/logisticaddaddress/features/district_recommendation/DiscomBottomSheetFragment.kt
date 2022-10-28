package com.tokopedia.logisticaddaddress.features.district_recommendation

import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.beloo.widget.chipslayoutmanager.ChipsLayoutManager
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.recyclerview.EndlessRecyclerViewScrollListener
import com.tokopedia.logisticCommon.data.entity.response.Data
import com.tokopedia.logisticaddaddress.R
import com.tokopedia.logisticaddaddress.common.AddressConstants.*
import com.tokopedia.logisticaddaddress.databinding.BottomsheetDistrictRecommendationBinding
import com.tokopedia.logisticaddaddress.di.DaggerDistrictRecommendationComponent
import com.tokopedia.logisticaddaddress.domain.model.Address
import com.tokopedia.logisticaddaddress.features.addnewaddress.ChipsItemDecoration
import com.tokopedia.logisticaddaddress.features.addnewaddress.analytics.AddNewAddressAnalytics
import com.tokopedia.logisticaddaddress.features.district_recommendation.adapter.DiscomNewAdapter
import com.tokopedia.logisticaddaddress.features.district_recommendation.adapter.PopularCityAdapter
import com.tokopedia.network.utils.ErrorHandler
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.utils.lifecycle.autoCleared
import rx.Emitter
import rx.Observable
import rx.Subscription
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import rx.subscriptions.CompositeSubscription
import java.util.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject

/**
 * Created by fwidjaja on 2019-05-29.
 */
class DiscomBottomSheetFragment : BottomSheetUnify(),
        PopularCityAdapter.ActionListener,
        DiscomContract.View,
        DiscomNewAdapter.ActionListener {

    private lateinit var popularCityAdapter: PopularCityAdapter
    private lateinit var listDistrictAdapter: DiscomNewAdapter
    private lateinit var chipsLayoutManagerZipCode: ChipsLayoutManager
    private var isLoading: Boolean = false
    private var input: String = ""
    private var page: Int = 1
    private val mLayoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
    private val mEndlessListener = object : EndlessRecyclerViewScrollListener(mLayoutManager) {
        override fun onLoadMore(page: Int, totalItemsCount: Int) {
            presenter.loadData(input, page + 1)
        }
    }
    private var mIsInitialLoading: Boolean = false
    private val mCompositeSubs: CompositeSubscription = CompositeSubscription()
    private val handler = Handler()
    private var actionListener: ActionListener? = null
    private var isFullFlow: Boolean = true
    private var isLogisticLabel: Boolean = true
    private var districtAddressData: Address? = null
    private var isPinpoint: Boolean = false

    private var binding by autoCleared<BottomsheetDistrictRecommendationBinding>()

    @Inject
    lateinit var presenter: DiscomContract.Presenter

    @Inject
    lateinit var userSession: UserSessionInterface

    init {
        isFullpage = true
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            isFullFlow = it.getBoolean(EXTRA_IS_FULL_FLOW, true)
            isLogisticLabel = it.getBoolean(EXTRA_IS_LOGISTIC_LABEL, true)
            isPinpoint = it.getBoolean(EXTRA_IS_PINPOINT, false)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        initView()
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    fun initView() {
        binding = BottomsheetDistrictRecommendationBinding.inflate(LayoutInflater.from(context), null, false)
        setChild(binding.root)
        setTitle(getString(R.string.kota_kecamatan))
        setCloseClickListener {
            AddNewAddressAnalytics.eventClickBackArrowOnNegativePage(isFullFlow, isLogisticLabel)
            dismiss()
        }

        prepareLayout()
        initInjector()
        setViewListener()
    }

    private fun prepareLayout() {
        binding.llListDistrict.visibility = View.GONE

        val cityList = resources.getStringArray(R.array.cityList)
        val chipsLayoutManager = ChipsLayoutManager.newBuilder(binding.root.context)
                .setOrientation(ChipsLayoutManager.HORIZONTAL)
                .setRowStrategy(ChipsLayoutManager.STRATEGY_DEFAULT)
                .build()

        chipsLayoutManagerZipCode = ChipsLayoutManager.newBuilder(binding.root.context)
                .setOrientation(ChipsLayoutManager.HORIZONTAL)
                .setRowStrategy(ChipsLayoutManager.STRATEGY_DEFAULT)
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

        binding.rvListDistrict.apply {
            layoutManager = mLayoutManager
            adapter = listDistrictAdapter
        }

        binding.layoutSearch.searchBarTextField.setSelection(binding.layoutSearch.searchBarTextField.text.length)
    }

    override fun onDetach() {
        super.onDetach()
        presenter.detach()
        mCompositeSubs.unsubscribe()
    }

    override fun renderData(list: List<Address>, hasNextPage: Boolean) {
        isLoading = false
        binding.llPopularCity.visibility = View.GONE
        binding.llListDistrict.visibility = View.VISIBLE
        binding.tvDescInputDistrict.visibility = View.VISIBLE
        binding.tvDescInputDistrict.setText(R.string.hint_advice_search_address)
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
        //no-op
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
        binding.layoutSearch.searchBarTextField.setText(city)
        binding.layoutSearch.searchBarTextField.setSelection(city.length)
        AddNewAddressAnalytics.eventClickChipsKotaKecamatanChangeAddressNegative(isFullFlow, isLogisticLabel)
    }

    override fun showToasterError(message: String) {
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
        binding.layoutSearch.searchBarTextField.isFocusableInTouchMode = true
        watchTextRx(binding.layoutSearch.searchBarTextField)
            .subscribe { s ->
                if (s.isNotEmpty()) {
                    input = s
                    mIsInitialLoading = true
                    handler.postDelayed({
                        presenter.loadData(input, page)
                    }, DELAY_MILIS)
                } else {
                    binding.tvDescInputDistrict.visibility = View.GONE
                    binding.llPopularCity.visibility = View.VISIBLE
                    binding.llListDistrict.visibility = View.GONE
                }
            }.toCompositeSubs()

        binding.rvListDistrict.addOnScrollListener(mEndlessListener)

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
            .debounce(DEBOUNCE, TimeUnit.MILLISECONDS)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    private fun Subscription.toCompositeSubs() {
        mCompositeSubs.add(this)
    }

    override fun onDistrictItemClicked(districtModel: Address) {
        context?.let {
            districtModel.run {
                actionListener?.onGetDistrict(districtModel)
                AddNewAddressAnalytics.eventClickSuggestionKotaKecamatanChangeAddressNegative(isFullFlow, isLogisticLabel)
                dismiss()
            }
        }
    }

    fun getDistrict(data: Address) {
        districtAddressData = data
        binding.layoutSearch.visibility = View.GONE
        binding.mapSearchDivider1.visibility = View.GONE
        binding.tvDescInputDistrict.visibility = View.GONE
        binding.llListDistrict.visibility = View.GONE
        binding.llPopularCity.visibility = View.GONE
    }

    interface ActionListener {
        fun onGetDistrict(districtAddress: Address)
        fun onChooseZipcode(districtAddress: Address, zipCode: String, isPinpoint: Boolean)
    }

    companion object {

        private const val DEBOUNCE: Long = 700
        private const val DELAY_MILIS: Long = 200

        @JvmStatic
        fun newInstance(isLogisticLabel: Boolean, isAnaRevamp: Boolean, isPinpoint: Boolean?): DiscomBottomSheetFragment {
            return DiscomBottomSheetFragment().apply {
                arguments = Bundle().apply {
                    putBoolean(EXTRA_IS_LOGISTIC_LABEL, isLogisticLabel)
                    putBoolean(EXTRA_IS_ANA_REVAMP, isAnaRevamp)
                    isPinpoint?.let { putBoolean(EXTRA_IS_PINPOINT, it) }
                }
            }
        }
    }

}
