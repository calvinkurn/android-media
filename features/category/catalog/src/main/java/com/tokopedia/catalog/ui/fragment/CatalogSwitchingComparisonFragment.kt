package com.tokopedia.catalog.ui.fragment

import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.recyclerview.widget.AsyncDifferConfig
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SimpleItemAnimator
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.common.utils.view.KeyboardHandler
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.catalog.R
import com.tokopedia.catalog.base.BaseSimpleListFragment
import com.tokopedia.catalog.databinding.FragmentCatalogSwitchingComparisonBinding
import com.tokopedia.catalog.di.DaggerCatalogComponent
import com.tokopedia.catalog.ui.adapter.CatalogListingAdapterDiffUtil
import com.tokopedia.catalog.ui.adapter.CatalogListingListener
import com.tokopedia.catalog.ui.adapter.CatalogListingSwitchingCatalogPageDiffer
import com.tokopedia.catalog.ui.adapter.CatalogSelectionAdapter
import com.tokopedia.catalog.ui.adapter.CatalogSelectionListener
import com.tokopedia.catalog.ui.bottomsheet.ResetCatalogBottomSheet
import com.tokopedia.catalog.ui.model.CatalogComparisonProductsUiModel
import com.tokopedia.catalog.ui.viewmodel.CatalogSwitchComparisonViewModel
import com.tokopedia.dialog.DialogUnify
import com.tokopedia.globalerror.GlobalError.Companion.NO_CONNECTION
import com.tokopedia.globalerror.GlobalError.Companion.SERVER_ERROR
import com.tokopedia.imageassets.TokopediaImageUrl.CATALOG_EMPTY_STATE
import com.tokopedia.kotlin.extensions.orFalse
import com.tokopedia.kotlin.extensions.view.EMPTY
import com.tokopedia.kotlin.extensions.view.ONE
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.isVisible
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.kotlin.extensions.view.removeFirst
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.media.loader.loadImage
import com.tokopedia.network.utils.ErrorHandler
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.utils.lifecycle.autoClearedNullable
import kotlinx.android.synthetic.main.fragment_catalog_switching_comparison.*
import kotlinx.android.synthetic.main.layout_empty_catalog_listing_switching_page.view.*
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.inject.Inject
import com.tokopedia.catalog.R as catalogR
import com.tokopedia.unifyprinciples.R as unifyprinciplesR

class CatalogSwitchingComparisonFragment :
    BaseSimpleListFragment<CatalogListingAdapterDiffUtil, CatalogComparisonProductsUiModel.CatalogComparisonUIModel>(),
    CatalogListingListener,
    CatalogSelectionListener {

    @Inject
    lateinit var viewModel: CatalogSwitchComparisonViewModel

    private var binding by autoClearedNullable<FragmentCatalogSwitchingComparisonBinding>()

    private var catalogSelectionAdapter: CatalogSelectionAdapter? = null

    private val handlerDelayToaster = Handler(Looper.getMainLooper())

    private val normalWidthEditText: Int by lazy { binding?.sbCatalog?.width.orZero() }

    private var currentPage = 0

    private var initialDataFromSearch = false

    companion object {

        const val CATALOG_CHANGE_COMPARISON_TAG = "CATALOG_CHANGE_COMPARISON_TAG"
        const val ARG_CATALOG_ID = "catalog_id"
        const val ARG_COMPARISON_CATALOG_ID = "comparison_catalog_id"
        const val ARG_EXTRA_CATALOG_BRAND = "ARG_EXTRA_CATALOG_BRAND"
        const val ARG_EXTRA_CATALOG_CATEGORY_ID = "ARG_EXTRA_CATALOG_CATEGORY_ID"
        const val LIMIT_SELECT_PRODUCT = 5
        const val MINIMUM_SELECT_PRODUCT = 2
        private const val LIMIT = 10
        private const val DELAY_DEBOUNCE_SEARCH = 500L
        private const val DELAY_TOASTER = 1500L
        fun newInstance(
            catalogId: String,
            comparisonCatalogId: List<String>,
            categoryId: String,
            brand: String
        ): CatalogSwitchingComparisonFragment {
            val fragment = CatalogSwitchingComparisonFragment()
            val bundle = Bundle()
            bundle.putString(ARG_CATALOG_ID, catalogId)
            bundle.putStringArrayList(ARG_COMPARISON_CATALOG_ID, ArrayList(comparisonCatalogId))
            bundle.putString(ARG_EXTRA_CATALOG_CATEGORY_ID, categoryId)
            bundle.putString(ARG_EXTRA_CATALOG_BRAND, brand)
            fragment.arguments = bundle
            return fragment
        }
    }

    private var catalogId = ""
    private var compareCatalogId = listOf<String>()
    private var defaultComparison = listOf<String>()
    private var defaultComparisonlist =
        mutableListOf<CatalogComparisonProductsUiModel.CatalogComparisonUIModel>()
    private var brand = ""
    private var categoryId = ""
    private var searchKeyword = ""
    private val itemList =
        mutableListOf<CatalogComparisonProductsUiModel.CatalogComparisonUIModel>()
    private var view: View? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCatalogSwitchingComparisonBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        setInitialPage(Int.ONE)
        if (arguments != null) {
            catalogId = requireArguments().getString(ARG_CATALOG_ID, "")
            compareCatalogId =
                requireArguments().getStringArrayList(ARG_COMPARISON_CATALOG_ID).orEmpty()
            brand = requireArguments().getString(ARG_EXTRA_CATALOG_BRAND, "")
            categoryId = requireArguments().getString(ARG_EXTRA_CATALOG_CATEGORY_ID, "")
            defaultComparison = compareCatalogId
        }
        super.onCreate(savedInstanceState)
        adapter?.currentCatalogSelection = compareCatalogId
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        this.view = view
        setupToolbar()
        super.onViewCreated(view, savedInstanceState)
        binding?.rvCatalogList?.apply {
            (itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false
        }
        setupCatalogSelection(view.context)
        setupSearchBar()
        setupObservers(view)
        binding?.btnSeeCompare?.setOnClickListener {
            val intent = Intent().apply {
                putStringArrayListExtra(ARG_COMPARISON_CATALOG_ID, ArrayList(compareCatalogId))
            }
            activity?.setResult(Activity.RESULT_OK, intent)
            activity?.finish()
        }
        binding?.sbCatalog?.searchBarTextField?.setOnTouchListener { v, event ->
            binding?.sbCatalog?.searchBarTextField?.isCursorVisible = true
            if (binding?.tvTitle?.isVisible.orFalse()) {
                binding?.tvTitle?.gone()
                expandCollapseSearchWidth()
            }
            false
        }
        binding?.globalError?.setActionClickListener {
            loadInitialData()
        }
        hideKeyboardWhenTouchOutside()
    }

    override fun loadInitialData() {
        binding?.globalError?.gone()
        binding?.includeEmptyState?.root?.gone()
        visibilityLoaderForInitialPage(false)
        super.loadInitialData()
        checkingEnableOrDisableButtonSeeCompare()
    }

    override fun createAdapter(): CatalogListingAdapterDiffUtil {
        val asyncDifferConfig =
            AsyncDifferConfig.Builder(CatalogListingSwitchingCatalogPageDiffer())
                .build()

        return CatalogListingAdapterDiffUtil(asyncDifferConfig, listener = this)
    }

    override fun getRecyclerView(view: View): RecyclerView? = binding?.rvCatalogList

    override fun getSwipeRefreshLayout(view: View): SwipeRefreshLayout? {
        return null
    }

    override fun getPerPage(): Int = LIMIT

    override fun loadData(page: Int) {
        currentPage = page
        if (isLoadingInitialData && !initialDataFromSearch) {
            viewModel.loadAllDataInOnePage(
                catalogId = catalogId,
                brand = String.EMPTY,
                categoryId = categoryId,
                limit = getPerPage(),
                compareCatalogId = compareCatalogId,
                name = searchKeyword
            )
        } else {
            viewModel.getComparisonProducts(
                catalogId,
                "",
                categoryId,
                getPerPage(),
                page,
                searchKeyword
            )
        }
    }

    override fun onShowLoading() {
        if (isLoadingInitialData && !initialDataFromSearch) {
            visibilityLoaderForInitialPage(true)
        } else if (isLoadingInitialData && initialDataFromSearch) {
            binding?.rvCatalogList?.gone()
            binding?.loaderCatalogList?.show()
        }
    }

    override fun onHideLoading() {
        if (isLoadingInitialData && !initialDataFromSearch) {
            isLoadingInitialData = false
            visibilityLoaderForInitialPage(false)
        } else if (isLoadingInitialData && initialDataFromSearch) {
            initialDataFromSearch = false
            isLoadingInitialData = false
            binding?.rvCatalogList?.visible()
            binding?.loaderCatalogList?.gone()
        }
    }

    override fun onDataEmpty() {
        binding?.includeEmptyState?.ilEmpty?.loadImage(CATALOG_EMPTY_STATE)
        binding?.includeEmptyState?.root?.show()
    }

    override fun onGetListError(message: String) {
    }

    override fun onSwipeRefreshPulled() {
    }

    override fun clearAdapterData() {
        itemList.clear()
        adapter?.isShowLoadMore = false
        adapter?.notifyDataSetChanged()
        adapter?.submitList(null)
    }

    override fun addElementToAdapter(list: List<CatalogComparisonProductsUiModel.CatalogComparisonUIModel>) {
        itemList.addAll(list)
        adapter?.isShowLoadMore = list.size == getPerPage()
        adapter?.notifyDataSetChanged()
        adapter?.submitList(itemList.toList())
    }

    override fun onCheckListener(
        item: CatalogComparisonProductsUiModel.CatalogComparisonUIModel,
        isChecked: Boolean
    ) {
        val listCatalogId = compareCatalogId.toMutableList()
        if (!isChecked) {
            listCatalogId.remove(item.id)
            catalogSelectionAdapter?.itemList = catalogSelectionAdapter?.itemList?.filter {
                it.id != item.id.orEmpty()
            }.orEmpty().toMutableList()
            showMessageUnselectCatalog()
        } else {
            if (listCatalogId.size < (LIMIT_SELECT_PRODUCT)) {
                listCatalogId.add(item.id.orEmpty())
                catalogSelectionAdapter?.itemList?.add(item)
            }
        }
        compareCatalogId = listCatalogId
        adapter?.currentCatalogSelection = listCatalogId

        recyclerView?.post {
            catalogSelectionAdapter?.notifyDataSetChanged()
            adapter?.notifyItemRangeChanged(Int.ZERO, adapter?.currentList?.size.orZero())
        }
        checkingEnableOrDisableButtonSeeCompare()
    }

    override fun onActionListener(id: String) {
        val listCatalogId = compareCatalogId.toMutableList()
        listCatalogId.remove(id)
        adapter?.currentCatalogSelection = listCatalogId
        compareCatalogId = listCatalogId
        catalogSelectionAdapter?.itemList?.removeFirst {
            it.id == id
        }
        catalogSelectionAdapter?.notifyDataSetChanged()
        adapter?.notifyDataSetChanged()
        showMessageUnselectCatalog()
        checkingEnableOrDisableButtonSeeCompare()
    }

    override fun onDestroy() {
        super.onDestroy()
        handlerDelayToaster.removeCallbacksAndMessages(null)
    }

    private fun setupSearchBar() = binding?.apply {
        sbCatalog.clearListener = {
            searchKeyword = String.EMPTY
            initialDataFromSearch = true
            isLoadingInitialData = false
            loadInitialData()
            closeKeyboard(sbCatalog.rootView, sbCatalog.rootView.context)
        }

        sbCatalog.addDebouncedTextChangedListener(onTextChanged = {
            searchKeyword = binding?.sbCatalog?.searchBarTextField?.text?.toString().orEmpty()
            initialDataFromSearch = true
            isLoadingInitialData = false
            loadInitialData()
            binding?.tvTitle?.visible()
            expandCollapseSearchWidth()
        }, DELAY_DEBOUNCE_SEARCH)
    }

    override fun getScreenName() =
        CatalogSwitchingComparisonFragment::class.java.canonicalName.orEmpty()

    override fun initInjector() {
        DaggerCatalogComponent.builder()
            .baseAppComponent((activity?.applicationContext as? BaseMainApplication)?.baseAppComponent)
            .build()
            .inject(this)
    }

    private fun setupToolbar() = binding?.apply {
        toolbar.cartButton?.gone()
        toolbar.shareButton?.gone()
        toolbar.moreMenuButton?.gone()
        toolbar.searchButton?.gone()
        toolbar.reloadButton?.visible()
        toolbar.title = getString(catalogR.string.catalog_title_change_comparison)
        toolbar.subTitle = getString(catalogR.string.catalog_subtitle_change_comparison)
        toolbar.setNavigationOnClickListener {
            onFragmentBackPressed()
        }
        toolbar.reloadButton?.setOnClickListener {
            showResetCatalogBottomSheet()
        }
        toolbar.setColors(
            MethodChecker.getColor(
                context,
                unifyprinciplesR.color.Unify_NN950
            )
        )
    }

    private fun showResetCatalogBottomSheet() {
        val resetCatalogBottomSheet = ResetCatalogBottomSheet.createInstance()
        resetCatalogBottomSheet.show(childFragmentManager)
        resetCatalogBottomSheet.setOnClickReset {
            resetCatalogSelection()
        }

        resetCatalogBottomSheet.setOnClickRemove {
            removeAllCatalogSelection()
        }
    }

    private fun removeAllCatalogSelection() {
        compareCatalogId = listOf(compareCatalogId.first())
        val emptyCompareCatalog = catalogSelectionAdapter?.itemList?.filter {
            it.id == catalogId
        }.orEmpty()
        catalogSelectionAdapter?.itemList = emptyCompareCatalog.toMutableList()
        catalogSelectionAdapter?.notifyDataSetChanged()
        adapter?.currentCatalogSelection = compareCatalogId
        adapter?.notifyItemRangeChanged(Int.ZERO, adapter?.currentList?.size.orZero())
        checkingEnableOrDisableButtonSeeCompare()
    }

    private fun setupObservers(view: View) {
        viewModel.comparisonUiModel.observe(viewLifecycleOwner) {
            if (it == null) {
                Toaster.build(
                    view,
                    getString(R.string.catalog_error_message_inactive)
                ).show()
            } else {
                defaultComparisonlist.addAll(it.catalogComparisonList)
                catalogSelectionAdapter?.itemList?.addAll(it.catalogComparisonList)
                catalogSelectionAdapter?.notifyDataSetChanged()
            }
        }

        viewModel.catalogListingUiModel.observe(viewLifecycleOwner) {
            if (it != null) {
                val hasNextPage =
                    it.catalogComparisonList.size == getPerPage()
                renderList(it.catalogComparisonList, hasNextPage)
            }
        }

        viewModel.errorsToasterGetInitComparison.observe(viewLifecycleOwner) { throwable ->
            val errorMessage = ErrorHandler.getErrorMessage(view.context, throwable)
            when (throwable) {
                is UnknownHostException, is SocketTimeoutException -> {
                    binding?.globalError?.setType(NO_CONNECTION)
                }

                else -> {
                    binding?.globalError?.setType(SERVER_ERROR)
                }
            }
            binding?.globalError?.errorDescription?.text = errorMessage
            binding?.globalError?.show()
            visibilityLoaderForInitialPage(false, isInitLoadFailed = true)
        }

        viewModel.errorsToasterGetCatalogListing.observe(viewLifecycleOwner) {
            adapter?.isShowLoadMore = false
            adapter?.notifyDataSetChanged()
            Toaster.build(
                view,
                type = Toaster.TYPE_ERROR,
                text = getString(catalogR.string.catalog_message_failed_load_initial_data),
                actionText = getString(catalogR.string.catalog_label_oke),
                duration = Toaster.LENGTH_INDEFINITE
            ).show()
        }
    }

    private fun setupCatalogSelection(context: Context) = binding?.apply {
        catalogSelectionAdapter =
            CatalogSelectionAdapter(listener = this@CatalogSwitchingComparisonFragment)
        rvCatalogSelection.apply {
            adapter = catalogSelectionAdapter
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        }
    }

    private fun resetCatalogSelection() {
        catalogSelectionAdapter?.itemList?.clear()
        catalogSelectionAdapter?.itemList?.addAll(defaultComparisonlist)
        adapter?.currentCatalogSelection = defaultComparison
        compareCatalogId = defaultComparison
        catalogSelectionAdapter?.notifyDataSetChanged()
        adapter?.notifyItemRangeChanged(Int.ZERO, adapter?.currentList?.size.orZero())
        checkingEnableOrDisableButtonSeeCompare()
    }

    private fun checkingEnableOrDisableButtonSeeCompare() {
        val listCatalogId = compareCatalogId.toMutableList()
        binding?.btnSeeCompare?.isEnabled =
            (
                listCatalogId.size >= MINIMUM_SELECT_PRODUCT &&
                    compareCatalogId.joinToString(",") != defaultComparison.joinToString(",")
                )
    }

    private fun showMessageUnselectCatalog() {
        val totalUnselect = (LIMIT_SELECT_PRODUCT) - compareCatalogId.size
        val errorMessage =
            getString(catalogR.string.catalog_message_unselect, totalUnselect.toString())

        view?.let {
            if (totalUnselect == 4) {
                val toasterInfo = Toaster.build(
                    it,
                    getString(catalogR.string.catalog_info_message_min_max_total_selection_catalog),
                    Toaster.LENGTH_INDEFINITE,
                    Toaster.TYPE_NORMAL,
                    getString(catalogR.string.catalog_label_oke)
                )
                toasterInfo.show()
            }
        }
    }

    private fun expandCollapseSearchWidth() {
        val initialWidth = normalWidthEditText

        val finalWidth =
            binding?.clSearch?.width.orZero() - (binding?.clSearch?.paddingStart.orZero() + binding?.clSearch?.paddingEnd.orZero())

        val animator = ValueAnimator.ofInt(
            initialWidth,
            if (!binding?.tvTitle?.isVisible.orFalse()) finalWidth else normalWidthEditText
        )
        animator.duration = 250 // Adjust the duration as needed

        animator.addUpdateListener { animation ->
            val layoutParams = binding?.sbCatalog?.layoutParams
            layoutParams?.width = animation.animatedValue as Int
            binding?.sbCatalog?.layoutParams = layoutParams
        }

        animator.start()
    }

    private fun closeKeyboard(view: View, context: Context) {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

    private fun visibilityLoaderForInitialPage(isShow: Boolean, isInitLoadFailed: Boolean = false) = binding.apply {
        loaderCatalogList.showWithCondition(isShow)
        loaderSearch.showWithCondition(isShow)
        loaderCatalogSelection.showWithCondition(isShow)
        clSearch.showWithCondition(!isShow && !isInitLoadFailed)
        btnSeeCompare.showWithCondition(!isShow && !isInitLoadFailed)
        rvCatalogSelection.showWithCondition(!isShow && !isInitLoadFailed)
        divider.showWithCondition(!isInitLoadFailed)
    }

    private fun showCancelSwitch(
        title: String = getString(catalogR.string.catalog_switching_confirm_exit_title),
        description: String = getString(catalogR.string.catalog_switching_confirm_desc_title)
    ) {
        context?.let {
            DialogUnify(it, DialogUnify.VERTICAL_ACTION, DialogUnify.NO_IMAGE).apply {
                setTitle(title)
                setDescription(description)
                setPrimaryCTAText(it.resources.getString(catalogR.string.catalog_cancel))
                setSecondaryCTAText(it.resources.getString(catalogR.string.catalog_keep_back))
                setPrimaryCTAClickListener {
                    dismiss()
                }
                setSecondaryCTAClickListener {
                    activity?.finish()
                }
            }.show()
        }
    }

    override fun onFragmentBackPressed(): Boolean {
        showCancelSwitch()
        return true
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun hideKeyboardWhenTouchOutside() {
        binding?.rvCatalogList?.setOnTouchListener { _, _ ->
            activity?.apply {
                KeyboardHandler.hideSoftKeyboard(this)
            }
            false
        }

        binding?.rvCatalogSelection?.setOnTouchListener { _, event ->
            activity?.apply {
                KeyboardHandler.hideSoftKeyboard(this)
            }
            false
        }

        binding?.divider?.setOnTouchListener { _, _ ->
            activity?.apply {
                KeyboardHandler.hideSoftKeyboard(this)
            }
            true
        }

        binding?.toolbar?.setOnTouchListener { _, _ ->
            activity?.apply {
                KeyboardHandler.hideSoftKeyboard(this)
            }
            true
        }

        binding?.clSearch?.setOnTouchListener { _, _ ->
            activity?.apply {
                KeyboardHandler.hideSoftKeyboard(this)
            }
            true
        }

        binding?.mainLayout?.setOnTouchListener { _, _ ->
            activity?.apply {
                KeyboardHandler.hideSoftKeyboard(this)
            }
            true
        }
    }
}
