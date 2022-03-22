package com.tokopedia.createpost.view.plist

import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.coachmark.CoachMark2
import com.tokopedia.coachmark.CoachMark2Item
import com.tokopedia.createpost.common.analyics.CreatePostAnalytics
import com.tokopedia.createpost.common.di.CreatePostCommonModule
import com.tokopedia.createpost.common.view.plist.*
import com.tokopedia.createpost.createpost.R
import com.tokopedia.createpost.di.CreatePostModule
import com.tokopedia.createpost.di.DaggerCreatePostComponent
import com.tokopedia.createpost.view.listener.SearchCategoryBottomSheetListener
import com.tokopedia.createpost.view.bottomSheet.SearchCategoryTypeBottomSheet
import com.tokopedia.createpost.view.bottomSheet.SearchTypeData
import com.tokopedia.createpost.view.plist.ShopProductListActivity.Companion.PARAM_SHOP_BADGE
import com.tokopedia.createpost.view.plist.ShopProductListActivity.Companion.PARAM_SHOP_ID
import com.tokopedia.createpost.view.plist.ShopProductListActivity.Companion.PARAM_SHOP_NAME
import com.tokopedia.createpost.view.plist.ShopProductListActivity.Companion.PARAM_SOURCE
import com.tokopedia.createpost.view.util.CreatePostPrefs
import com.tokopedia.kotlin.extensions.view.afterTextChanged
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.library.baseadapter.AdapterCallback
import com.tokopedia.unifycomponents.ChipsUnify
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.unifyprinciples.Typography
import kotlinx.android.synthetic.main.fragment_shop_plist_page.view.*
import kotlinx.android.synthetic.main.layout_parent_product_list.*
import javax.inject.Inject

class ShopProductListFragment : BaseDaggerFragment(), AdapterCallback, ShopPageListener,
    SearchCategoryBottomSheetListener {

    @Inject
    lateinit var createPostAnalytics: CreatePostAnalytics
    lateinit var sortListItems: List<ShopPagePListSortItem>
    lateinit var headerViewText: Typography
    lateinit var searchCategoryBottomSheet:SearchCategoryTypeBottomSheet
    val coachMarkItem = ArrayList<CoachMark2Item>()
    private  lateinit var coachMark: CoachMark2


    val presenter: ShopPageProductListViewModel by lazy { ViewModelProviders.of(this)[ShopPageProductListViewModel::class.java] }
    var getImeiBS: ShopPListSortFilterBs? = null
    private val mAdapter: ShopProductListBaseAdapter by lazy {
        ShopProductListBaseAdapter(
            presenter,
            this,
            this
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.layout_parent_product_list, container, false)
        initViews(view)
        return view
    }
    override fun initInjector() {
        DaggerCreatePostComponent.builder()
            .createPostCommonModule(CreatePostCommonModule(requireContext().applicationContext))
            .createPostModule(CreatePostModule(requireContext().applicationContext)).build()
            .inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initListener()

        if (arguments != null) {
            presenter.getPageData(arguments?.getString(PARAM_SHOP_ID), arguments?.getString(
                PARAM_SOURCE))
        }
    }

    private fun initViews(view: View) {
        headerViewText = view.findViewById(R.id.search_type_header)

       headerViewText.text = MethodChecker.fromHtml(String.format(getString(R.string.content_creation_tagging_page_header_text)," Tokopedia"))

        view.recycler_view.layoutManager = GridLayoutManager(activity, 2)
        view.recycler_view.adapter = mAdapter

        mAdapter.resetAdapter()
        view.sb_shop_product.searchBarIcon.setImageDrawable(null)
        presenter.getSortData()
        val shopName = arguments?.getString(PARAM_SHOP_NAME) ?: ""
        val shopBadge = arguments?.getString(PARAM_SHOP_BADGE) ?: ""
        val shopNamePlaceHolderText = String.format(
            requireContext().getString(R.string.feed_product_page_search_bar_placeholder_text),
            shopName)

        view.search_type_header.setOnClickListener {
           openBottomSheet(shopName, shopBadge)
        }

        view.sb_shop_product.searchBarPlaceholder = shopNamePlaceHolderText
        view.cu_sort_chip.chip_container.setOnClickListener {
            createPostAnalytics.eventClickOnSortButton()
            getImeiBS = ShopPListSortFilterBs.newInstance(presenter, this, sortListItems)
            fragmentManager?.let { fm -> getImeiBS?.show(fm, "") }
        }
        view.cu_sort_chip.chip_right_icon.visible()
        view.cu_sort_chip.setChevronClickListener {
            createPostAnalytics.eventClickOnSortButton()
            getImeiBS = ShopPListSortFilterBs.newInstance(presenter, this, sortListItems)
            fragmentManager?.let { fm -> getImeiBS?.show(fm, "") }
        }

        view.sb_shop_product.searchBarTextField.afterTextChanged {
            mAdapter.filter.filter(it)
        }
        view.sb_shop_product.searchBarTextField.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus)
                createPostAnalytics.eventClickOnSearchBar()

        }
        view.sb_shop_product.iconDrawable = null
        view.sb_shop_product.searchBarTextField.setOnClickListener {
            createPostAnalytics.eventClickOnSearchBar()
        }

        coachMark = CoachMark2(activity as Context)

        coachMarkItem.add(
            CoachMark2Item(
                headerViewText,
                getString(R.string.content_creation_search_coachmark_header),
                getString(R.string.content_creation_search_coachmark_desc),
                CoachMark2.POSITION_BOTTOM
            )
        )
        if (CreatePostPrefs.getShouldShowCoachMarkValue(activity as Context))
            showFabCoachMark()

    }
    private fun showFabCoachMark() {
        CreatePostPrefs.saveShouldShowCoachMarkValue(activity as Context)
        if (::coachMark.isInitialized) {
            coachMark.showCoachMark(coachMarkItem)
        }
    }
    private fun openBottomSheet(shopName: String, shopBadge: String) {
        if (::coachMark.isInitialized) {
            coachMark.hideCoachMark()
        }
        searchCategoryBottomSheet = SearchCategoryTypeBottomSheet()
        context?.let {
            searchCategoryBottomSheet.show(
                Bundle.EMPTY,
                childFragmentManager, this, shopName, shopBadge, it
            )
        }
    }
    private fun addSortListObserver() = presenter.sortLiveData.observe(this, Observer {
        it?.let {
            when (it) {
                is Loading -> {
                }
                is Success -> {
                    sortListItems = it.data.sortData.result
                }
                is ErrorMessage -> {

                }
                else -> {

                }
            }
        }
    })

    private fun initListener() {
        if (view == null) {
            return
        }

        addListObserver()
        addSortValObserver()
        addProductValObserver()
        addBsObserver()
        addSortListObserver()
    }

    private fun addListObserver() = presenter.productList.observe(this, Observer {
        it?.let {
            when (it) {
                is Loading -> {
                    mAdapter.resetAdapter()
                    mAdapter.notifyDataSetChanged()
                }
                is Success -> {
                    mAdapter.onSuccess(it.data)
                }
                is ErrorMessage -> {
                    mAdapter.onError()
                }
            }
        }
    })

    private fun addSortValObserver() = presenter.newSortValeLiveData.observe(this, Observer {
        view?.cu_sort_chip?.chipText = it.name
        view?.cu_sort_chip?.chipType = ChipsUnify.TYPE_SELECTED
        presenter.getPageData(
            arguments?.getString("shopid"),
            arguments?.getString("source"),
            it.value.toString()
        )
    }

    )

    private fun addProductValObserver() =
        presenter.newProductValLiveData.observe(this, Observer { product ->
            activity?.let {
                val data = Intent();
                data.putExtra("product", product)
                it.setResult(RESULT_OK, data);
                it.finish();
            }
        }
        )

    private fun addBsObserver() =
        presenter.showBs.observe(this, Observer { product ->
            activity?.let {
                getImeiBS?.dismiss()
            }
        }
        )

    override fun onRetryPageLoad(pageNumber: Int) {
        presenter.getPageData(arguments?.getString(PARAM_SHOP_ID), arguments?.getString(
            PARAM_SHOP_NAME))
    }

    override fun onEmptyList(rawObject: Any) {
        container?.displayedChild = CONTAINER_EMPTY

    }

    override fun onStartFirstPageLoad() {
        showLoader()
    }

    override fun onFinishFirstPageLoad(count: Int, rawObject: Any?) {
        hideLoader()
    }

    override fun onStartPageLoad(pageNumber: Int) {

    }

    override fun onFinishPageLoad(itemCount: Int, pageNumber: Int, rawObject: Any?) {

    }

    override fun onError(pageNumber: Int) {
        if (pageNumber == 1) {
            container.displayedChild = CONTAINER_ERROR
        }
        Toaster.build(
            requireView(),
            getString(R.string.feed_content_product_list_page_error),
            Toaster.LENGTH_LONG,
            Toaster.TYPE_ERROR,
            getString(R.string.feed_content_coba_lagi_text),
            View.OnClickListener {
             onRetryPageLoad(1)
            }).show()
    }
    private fun showLoader() {
        container?.displayedChild = CONTAINER_LOADER
    }

    private fun hideLoader() {
        container?.displayedChild = CONTAINER_DATA
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }

    override fun getScreenName(): String {
        return SCREEN_NAME
    }

    companion object {
        private val CONTAINER_LOADER = 0
        private val CONTAINER_DATA = 1
        private val CONTAINER_EMPTY = 2
        private val CONTAINER_ERROR = 3
        private val SCREEN_NAME = "Product Tag Listing"

        fun newInstance(shopId: String, source: String, shopName: String, shopBadge: String): ShopProductListFragment {
            val bundle = Bundle()
            bundle.putString(PARAM_SHOP_ID, shopId)
            bundle.putString(PARAM_SOURCE, source)
            bundle.putString(PARAM_SHOP_NAME, shopName)
            bundle.putString(PARAM_SHOP_BADGE, shopBadge)
            val fragment = ShopProductListFragment()
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun shopProductImpressed(position: Int, product: ShopPageProduct) {
        createPostAnalytics.eventProductPageProductItemViewed(
            product,
            position)
    }

    override fun shopProductClicked(position: Int, product: ShopPageProduct) {
        createPostAnalytics.eventProductPageProductItemClicked(
            product,
            position)
    }
    override fun sortProductCriteriaClicked(criteria: String) {
        mAdapter.resetOriginalList()
        createPostAnalytics.eventClickOnSortCriteria(criteria)
    }

    override fun onItemCLick(searchTypeData: SearchTypeData) {
        if (::searchCategoryBottomSheet.isInitialized)
            searchCategoryBottomSheet.dismiss()
        if (::headerViewText.isInitialized)
            headerViewText.text = MethodChecker.fromHtml(String.format(getString(R.string.content_creation_tagging_page_header_text)," ${searchTypeData.text}"))
    }
}
