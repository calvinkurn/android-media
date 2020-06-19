package com.tokopedia.play.broadcaster.view.fragment

import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.transition.Fade
import androidx.transition.Slide
import androidx.transition.TransitionSet
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.play.broadcaster.R
import com.tokopedia.play.broadcaster.ui.model.result.NetworkResult
import com.tokopedia.play.broadcaster.util.compatTransitionName
import com.tokopedia.play.broadcaster.view.contract.PlayEtalaseSetupCoordinator
import com.tokopedia.play.broadcaster.view.custom.PlayBottomSheetHeader
import com.tokopedia.play.broadcaster.view.custom.PlaySearchBar
import com.tokopedia.play.broadcaster.view.fragment.base.PlayBaseEtalaseSetupFragment
import com.tokopedia.play.broadcaster.view.fragment.base.PlayBaseSetupFragment
import com.tokopedia.play.broadcaster.view.partial.BottomActionPartialView
import com.tokopedia.play.broadcaster.view.partial.SelectedProductPagePartialView
import com.tokopedia.play.broadcaster.view.viewmodel.PlayEtalasePickerViewModel
import com.tokopedia.unifycomponents.Toaster
import javax.inject.Inject

/**
 * Created by jegul on 26/05/20
 */
class PlayEtalasePickerFragment @Inject constructor(
        private val viewModelFactory: ViewModelFactory
) : PlayBaseSetupFragment(), PlayEtalaseSetupCoordinator {

    private lateinit var viewModel: PlayEtalasePickerViewModel

    private lateinit var container: ViewGroup
    private lateinit var tvInfo: TextView
    private lateinit var flEtalaseFlow: FrameLayout
    private lateinit var psbSearch: PlaySearchBar
    private lateinit var bottomSheetHeader : PlayBottomSheetHeader

    private lateinit var selectedProductPage: SelectedProductPagePartialView
    private lateinit var bottomActionView: BottomActionPartialView

    private val fragmentFactory: FragmentFactory
        get() = childFragmentManager.fragmentFactory

    private val currentFragment: Fragment?
        get() = childFragmentManager.findFragmentById(R.id.fl_etalase_flow)

    override fun getScreenName(): String = "Play Etalase Picker"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupTransition()
        viewModel = ViewModelProviders.of(requireParentFragment(), viewModelFactory).get(PlayEtalasePickerViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        postponeEnterTransition()
        return inflater.inflate(R.layout.fragment_play_etalase_picker, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView(view)
        setupView(view)

        showBottomAction(false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        observeSelectedProducts()
        observeUploadProduct()
    }

    override fun onInterceptBackPressed(): Boolean {
        return false
    }

    override fun openEtalaseDetail(etalaseId: String, sharedElements: List<View>) {
        bottomSheetCoordinator.navigateToFragment(
                PlayEtalaseDetailFragment::class.java,
                extras = Bundle().apply {
                    putString(PlayEtalaseDetailFragment.EXTRA_ETALASE_ID, etalaseId)
                },
                sharedElements = sharedElements
        )
    }

    override fun openSearchPage(keyword: String) {
        openFragment(
                PlaySearchSuggestionsFragment::class.java,
                extras = Bundle().apply {
                    putString(PlaySearchSuggestionsFragment.EXTRA_KEYWORD, keyword)
                }
        )
    }

    override fun openProductSearchPage(keyword: String) {
        openFragment(
                PlaySearchResultFragment::class.java,
                extras = Bundle().apply {
                    putString(PlaySearchResultFragment.EXTRA_KEYWORD, keyword)
                }
        )

        psbSearch.clearFocus()
        psbSearch.text = keyword
    }

    override fun goBack(clazz: Class<out Fragment>) {
        if (childFragmentManager.fragments.isNotEmpty()) childFragmentManager.popBackStack(clazz.name, 0)
    }

    private fun initView(view: View) {
        with(view) {
            container = this as ViewGroup
            tvInfo = findViewById(R.id.tv_info)
            flEtalaseFlow = findViewById(R.id.fl_etalase_flow)
            psbSearch = findViewById(R.id.psb_search)
            bottomSheetHeader = findViewById(R.id.bottom_sheet_header)
        }

        selectedProductPage = SelectedProductPagePartialView(view as ViewGroup, object : SelectedProductPagePartialView.Listener {
            override fun onProductSelectStateChanged(productId: Long, isSelected: Boolean) {
                viewModel.selectProduct(productId, isSelected)
                onSelectedProductChanged()
            }
        })

        bottomActionView = BottomActionPartialView(view as ViewGroup, object : BottomActionPartialView.Listener {
            override fun onInventoryIconClicked() {
                showSelectedProductPage()
            }

            override fun onNextButtonClicked() {
                uploadProduct()
            }
        })
    }

    private fun setupView(view: View) {
        psbSearch.setListener(object : PlaySearchBar.Listener {

            override fun onEditStateChanged(view: PlaySearchBar, isEditing: Boolean) {
                if (isEditing) openSearchPage(view.text)
            }

            override fun onCanceled(view: PlaySearchBar) {
                goBack(PlayEtalaseListFragment::class.java)
            }

            override fun onNewKeyword(view: PlaySearchBar, keyword: String) {
                val currFragment = currentFragment
                if (currFragment is PlaySearchSuggestionsFragment) currFragment.searchKeyword(keyword)
            }

            override fun onSearchButtonClicked(view: PlaySearchBar, keyword: String) {
                if (keyword.isNotEmpty()) openProductSearchPage(keyword)
                else psbSearch.cancel()
            }
        })

        if (currentFragment == null) openFragment(PlayEtalaseListFragment::class.java)

        bottomSheetHeader.setListener(object : PlayBottomSheetHeader.Listener {
            override fun onBackButtonClicked(view: PlayBottomSheetHeader) {
                bottomSheetCoordinator.goBack()
            }
        })
        bottomSheetHeader.setHeader(getString(R.string.play_etalase_picker_title), isRoot = true)
    }


    private fun openFragment(
            fragmentClass: Class<out Fragment>,
            extras: Bundle = Bundle.EMPTY,
            sharedElements: List<View> = emptyList(),
            onFragment: (Fragment) -> Unit = {}
    ): Fragment {
        val fragmentTransaction = childFragmentManager.beginTransaction()
        val destFragment = getFragmentByClassName(fragmentClass)
        destFragment.arguments = extras
        onFragment(destFragment)
        fragmentTransaction
                .apply {
                    sharedElements.forEach {
                        val transitionName = it.compatTransitionName
                        if (transitionName != null) addSharedElement(it, transitionName)
                    }

                    if (sharedElements.isNotEmpty()) setReorderingAllowed(true)
                }
                .replace(R.id.fl_etalase_flow, destFragment, fragmentClass.name)
                .addToBackStack(fragmentClass.name)
                .commit()

        return destFragment
    }

    private fun getFragmentByClassName(fragmentClass: Class<out Fragment>): Fragment {
        return fragmentFactory.instantiate(requireContext().classLoader, fragmentClass.name)
    }

    private fun showSelectedProductPage() {
        if (selectedProductPage.isShown) return

        selectedProductPage.setSelectedProductList(viewModel.selectedProductList)
        selectedProductPage.show()
    }

    override fun showBottomAction(shouldShow: Boolean) {
        if (shouldShow) bottomActionView.show() else bottomActionView.hide()
    }

    private fun onSelectedProductChanged() {
        (currentFragment as? PlayBaseEtalaseSetupFragment)?.refresh()
    }

    private fun showCoverTitlePage(nextBtnView: View) {
        bottomSheetCoordinator.navigateToFragment(
                fragmentClass = PlayCoverTitleSetupFragment::class.java,
                sharedElements = listOf(nextBtnView)
        )
    }

    private fun uploadProduct() {
        viewModel.uploadProduct(bottomSheetCoordinator.channelId)
    }

    /**
     * Observe
     */
    private fun observeSelectedProducts() {
        viewModel.observableSelectedProducts.observe(viewLifecycleOwner, Observer {
            bottomActionView.setupBottomActionWithProducts(it)
            selectedProductPage.onSelectedProductsUpdated(it)
        })
    }

    private fun observeUploadProduct() {
        viewModel.observableUploadProductEvent.observe(viewLifecycleOwner, Observer {
            when (it) {
                NetworkResult.Loading -> bottomActionView.setLoading(true)
                is NetworkResult.Fail -> {
                    bottomActionView.setLoading(false)
                    Toaster.make(requireView(), it.error.localizedMessage)
                }
                is NetworkResult.Success -> {
                    val data = it.data.getContentIfNotHandled()
                    if (data != null) {
                        bottomActionView.setLoading(false)
                        showCoverTitlePage(bottomActionView.getButtonView())
                    }
                }
            }
        })
    }

    /**
     * Transition
     */
    private fun setupTransition() {
        setupExitTransition()
        setupReenterTransition()
    }

    private fun setupExitTransition() {
        exitTransition = TransitionSet()
                .addTransition(Slide(Gravity.START))
                .addTransition(Fade(Fade.OUT))
                .setDuration(300)
    }

    private fun setupReenterTransition() {
        reenterTransition = TransitionSet()
                .addTransition(Slide(Gravity.START))
                .addTransition(Fade(Fade.IN))
                .setDuration(300)
    }

    companion object {

        private const val SPAN_COUNT = 2
    }
}