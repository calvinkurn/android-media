package com.tokopedia.topads.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.base.view.recyclerview.EndlessRecyclerViewScrollListener
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.filter.bottomsheet.filtergeneraldetail.FilterGeneralDetailBottomSheet
import com.tokopedia.filter.common.data.Filter
import com.tokopedia.filter.common.data.Option
import com.tokopedia.topads.create.databinding.MpAdGroupFragmentBinding
import com.tokopedia.topads.di.CreateAdsComponent
import com.tokopedia.topads.view.adapter.adgrouplist.AdGroupListAdapter
import com.tokopedia.topads.create.R
import com.tokopedia.topads.view.adapter.adgrouplist.model.ErrorUiModel
import com.tokopedia.topads.view.adapter.adgrouplist.typefactory.AdGroupTypeFactory
import com.tokopedia.topads.view.adapter.adgrouplist.typefactory.AdGroupTypeFactoryImpl
import com.tokopedia.topads.view.adapter.adgrouplist.viewholder.AdGroupViewHolder
import com.tokopedia.topads.view.adapter.adgrouplist.viewholder.ErrorViewHolder
import com.tokopedia.topads.view.model.MpAdsGroupsViewModel
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

class MpAdGroupFragment : BaseDaggerFragment(),
    AdGroupViewHolder.AdGroupListener,
    FilterGeneralDetailBottomSheet.Callback,
    ErrorViewHolder.ErrorListener {

    companion object{
        fun newInstance() : MpAdGroupFragment{
            return MpAdGroupFragment()
        }

        private const val IMPRESSION_VALUE = "impression"
        private const val CLICK_VALUE = "click"
        private const val CONVERSION_VALUE = "conversion"
    }

    private var binding:MpAdGroupFragmentBinding?=null
    var userSession:UserSessionInterface?=null
    @Inject set

    private var shopId = ""

    private val adGroupAdapter:AdGroupListAdapter by lazy {
        AdGroupListAdapter(getAdGroupTypeFactory())
    }
    private var linearLayoutManager:LinearLayoutManager?=null

    private var endlessScrollListener:EndlessRecyclerViewScrollListener?=null

    @Inject
    lateinit var viewModelFactory:ViewModelFactory
    private val adGroupViewModel:MpAdsGroupsViewModel by lazy {
        ViewModelProvider(this,viewModelFactory).get(MpAdsGroupsViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        shopId = userSession?.shopId.orEmpty()
    }

    override fun initInjector() {
        getComponent(CreateAdsComponent::class.java).inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = MpAdGroupFragmentBinding.inflate(inflater)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupHeader()
        setupRecyclerView()
        attachFilterClickListener()
        observeViewModel()
        adGroupViewModel.loadFirstPage(shopId)
    }

    private fun setupHeader(){
        binding?.adGroupHeader?.apply {
            setNavigationOnClickListener {
                activity?.onBackPressed()
            }
        }
    }

    private fun setupRecyclerView(){
        linearLayoutManager = LinearLayoutManager(context)
        createEndlessRecyclerViewListener()
        binding?.adGroupRv?.apply {
            layoutManager = linearLayoutManager
            adapter = adGroupAdapter
            endlessScrollListener?.let {
                addOnScrollListener(it)
            }
        }
    }

    private fun createEndlessRecyclerViewListener() {
       endlessScrollListener = object : EndlessRecyclerViewScrollListener(linearLayoutManager) {
            override fun onLoadMore(page: Int, totalItemsCount: Int) {
                adGroupViewModel.loadMorePages(shopId, page)
            }
        }
    }

    private fun observeViewModel(){
        adGroupViewModel.mainListLiveData.observe(viewLifecycleOwner,::submitListToAdapter)
        adGroupViewModel.hasNextLiveData.observe(viewLifecycleOwner,::onMoreGroupsLoaded)
    }

    private fun attachFilterClickListener(){
        binding?.adGroupFilterBtn?.setOnClickListener {
            showFilterBottomSheet()
        }
    }

    private fun getAdGroupTypeFactory() : AdGroupTypeFactory = AdGroupTypeFactoryImpl(this, this)


    @Suppress("UNCHECKED_CAST")
    private fun submitListToAdapter(list:List<Visitable<*>>){
        adGroupAdapter.submitList(list as List<Visitable<Any>>)
        if(list.filterIsInstance(ErrorUiModel::class.java).isNotEmpty())
            hideCTA()
        else showCTA()
    }

    private fun onMoreGroupsLoaded(hasNext:Boolean){
        if(!hasNext){
            removeRecyclerViewScrollListeners()
            return
        }
        endlessScrollListener?.updateStateAfterGetData()
        endlessScrollListener?.setHasNextPage(hasNext)
    }

    private fun removeRecyclerViewScrollListeners(){
        endlessScrollListener?.let {
            binding?.adGroupRv?.removeOnScrollListener(it)
        }
    }

    private fun hideCTA(){
        binding?.adGroupCta?.visibility = View.GONE
    }

    private fun showCTA(){
        binding?.adGroupCta?.visibility = View.VISIBLE
    }

    // Filter Logic
    private fun showFilterBottomSheet(){
      FilterGeneralDetailBottomSheet().also {
          it.show(
              fragmentManager = childFragmentManager,
              filter = getFilerData(),
              callback = this,
              buttonApplyFilterDetailText = context?.getString(R.string.ad_group_filter_bottomsheet_cta)
          )
      }
    }

    private fun getFilerData() = Filter(
        title = context?.getString(R.string.ad_group_filter_bottomsheet_title).orEmpty(),
        options = getFilterOptions()
    )

    private fun getFilterOptions() : List<Option>{
        val impressionText = context?.getString(R.string.ad_group_impression).orEmpty()
        val clickText = context?.getString(R.string.ad_group_click).orEmpty()
        val conversionText = context?.getString(R.string.ad_group_conversion).orEmpty()
        return listOf(
            Option(name = impressionText, key = impressionText, value = IMPRESSION_VALUE, inputType = Option.INPUT_TYPE_RADIO,
                inputState = "${isFilterOptionSelected(impressionText)}"),
            Option(name = clickText, key = clickText, value = CLICK_VALUE, inputType = Option.INPUT_TYPE_RADIO,
                inputState = "${isFilterOptionSelected(clickText)}"),
            Option(name = conversionText, key = conversionText, value = CONVERSION_VALUE, inputType = Option.INPUT_TYPE_RADIO,
                inputState = "${isFilterOptionSelected(conversionText)}")
        )
    }

    private fun isFilterOptionSelected(filterParam:String) = adGroupViewModel.sortParam == filterParam

    override fun onApplyButtonClicked(optionList: List<Option>?) {
        optionList?.let { it1 ->
            it1.forEach {
                if(it.inputState=="true"){
                  adGroupViewModel.sortParam = it.key
                  resetAdGroupList()
                }
            }
        }
    }
    // Filter Logic End

    //Call this method to reset the ad group list
    private fun resetAdGroupList(){
        adGroupViewModel.loadFirstPage(shopId)
    }

    override fun onAdStatClicked(bottomSheet: BottomSheetUnify) {
        bottomSheet.show(childFragmentManager,"")
    }

    override fun getScreenName() = ""

    // Error Logic

    override fun onErrorActionClicked() {

    }

}
