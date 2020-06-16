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
import androidx.lifecycle.ViewModelProviders
import androidx.transition.Fade
import androidx.transition.Slide
import androidx.transition.TransitionSet
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.play.broadcaster.R
import com.tokopedia.play.broadcaster.util.compatTransitionName
import com.tokopedia.play.broadcaster.view.contract.PlayEtalaseSetupCoordinator
import com.tokopedia.play.broadcaster.view.fragment.base.PlayBaseSetupFragment
import com.tokopedia.play.broadcaster.view.viewmodel.PlayEtalasePickerViewModel
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

    private val fragmentFactory: FragmentFactory
        get() = childFragmentManager.fragmentFactory

    private val currentFragment: Fragment?
        get() = childFragmentManager.findFragmentById(R.id.fl_etalase_flow)

    override fun refresh() {
//        etalaseAdapter.notifyDataSetChanged()
    }

    override fun getScreenName(): String = "Play Etalase Picker"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupTransition()
        viewModel = ViewModelProviders.of(requireParentFragment(), viewModelFactory).get(PlayEtalasePickerViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        bottomSheetCoordinator.showBottomAction(false)
        return inflater.inflate(R.layout.fragment_play_etalase_picker, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView(view)
        setupView(view)
        bottomSheetCoordinator.setupTitle(getString(R.string.play_etalase_picker_title))
    }

    override fun onInterceptBackPressed(): Boolean {
        return false
    }

    override fun openEtalaseDetail(etalaseId: String, sharedElements: List<View>) {
        bottomSheetCoordinator.navigateToFragment(
                PlayEtalaseDetailFragment::class.java,
                Bundle().apply {
                    putString(PlayEtalaseDetailFragment.EXTRA_ETALASE_ID, etalaseId)
                },
                sharedElements = sharedElements
        )
    }

    override fun openSearchPage(keyword: String, sharedElements: List<View>) {
        openFragment(PlaySearchSuggestionsFragment::class.java, sharedElements = sharedElements)
    }

    private fun initView(view: View) {
        with(view) {
            container = this as ViewGroup
            tvInfo = findViewById(R.id.tv_info)
            flEtalaseFlow = findViewById(R.id.fl_etalase_flow)
        }
    }

    private fun setupView(view: View) {
//        psbSearch.setListener(object : PlaySearchBar.Listener {
//
//            override fun onEditStateChanged(view: PlaySearchBar, isEditing: Boolean) {
//                if (isEditing) enterSearchMode()
//                else exitSearchMode()
//            }
//
//            override fun onCanceled(view: PlaySearchBar) {
//                exitSearchMode()
//            }
//
//            override fun onNewKeyword(view: PlaySearchBar, keyword: String) {
//                viewModel.loadSuggestionsFromKeyword(keyword)
//            }
//
//            override fun onSearchButtonClicked(view: PlaySearchBar, keyword: String) {
//                if (keyword.isNotEmpty()) shouldSearchProductWithKeyword(keyword)
//                else psbSearch.cancel()
//            }
//        })

        if (currentFragment == null) openFragment(PlayEtalaseListFragment::class.java)
    }

    private fun shouldSearchProductWithKeyword(keyword: String) {
//        scrollListener.resetState()
//        scrollListener.loadMoreNextPage()

//        exitSearchMode()
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
        return fragmentFactory.instantiate(fragmentClass.classLoader!!, fragmentClass.name)
    }

    /**
     * Transition
     */
    private fun onSearchModeTransition() {
//        TransitionManager.beginDelayedTransition(
//                container,
//                Slide(Gravity.BOTTOM)
//                        .addTarget(rvEtalase)
//                        .setDuration(300)
//                        .setStartDelay(200)
//                        .excludeChildren(psbSearch, true)
//        )
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
                .setStartDelay(200)
                .setDuration(300)
    }

    companion object {

        private const val SPAN_COUNT = 2
    }
}