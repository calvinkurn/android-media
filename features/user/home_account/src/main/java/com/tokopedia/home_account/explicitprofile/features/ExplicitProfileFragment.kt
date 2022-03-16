package com.tokopedia.home_account.explicitprofile.features

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.dialog.DialogUnify
import com.tokopedia.home_account.R
import com.tokopedia.home_account.databinding.FragmentExplicitProfileBinding
import com.tokopedia.home_account.explicitprofile.ExplicitProfileConstant.SCREEN_EXPLICIT_PROFILE
import com.tokopedia.home_account.explicitprofile.data.CategoriesDataModel
import com.tokopedia.home_account.explicitprofile.data.TemplateDataModel
import com.tokopedia.home_account.explicitprofile.di.component.ExplicitProfileComponentsBuilder
import com.tokopedia.home_account.explicitprofile.trackers.ExplicitProfileAnalytics
import com.tokopedia.home_account.explicitprofile.wrapper.ExplicitProfileResult
import com.tokopedia.media.loader.loadImage
import com.tokopedia.network.utils.ErrorHandler
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.utils.lifecycle.autoClearedNullable
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject

class ExplicitProfileFragment : BaseDaggerFragment() {

    @Inject
    lateinit var tracker: ExplicitProfileAnalytics

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val viewModelFragmentProvider by lazy {
        activity?.let {
            ViewModelProvider(it, viewModelFactory)
        }
    }

    private val viewModel by lazy { viewModelFragmentProvider?.get(ExplicitProfileViewModel::class.java) }
    private val viewModelShared by lazy { viewModelFragmentProvider?.get(ExplicitProfileSharedViewModel::class.java) }

    private var viewBinding by autoClearedNullable<FragmentExplicitProfileBinding>()
    private var pageAdapter: ExplicitProfilePageAdapter? = null
    private var categories: CategoriesDataModel = CategoriesDataModel()
    private val templatesDataModel: MutableList<TemplateDataModel> = mutableListOf()

    override fun getScreenName(): String = SCREEN_EXPLICIT_PROFILE

    override fun initInjector() {
        activity?.application?.let {
            ExplicitProfileComponentsBuilder
                .getComponent(it)
                .inject(this)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        viewBinding = FragmentExplicitProfileBinding.inflate(inflater, container, false)
        return viewBinding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initObservers()
        setupToolbar()
        setupViews()

        viewModel?.getAllCategories()
    }

    private fun initObservers() {
        viewModel?.explicitCategories?.observe(viewLifecycleOwner) {
            when(it) {
                is ExplicitProfileResult.Loading -> {
                    showLoading(true)
                }
                is ExplicitProfileResult.Success -> {
                    showLoading(false)
                    onSuccessGetCategories(it.data)
                }
                is ExplicitProfileResult.Failure -> {
                    showLoading(false)
                    onError(it.error)
                }
            }
        }

        viewModel?.saveAnswers?.observe(viewLifecycleOwner) {
            when(it) {
                is ExplicitProfileResult.Loading -> {
                    showLoading(true)
                }
                is ExplicitProfileResult.Failure -> {
                    tracker.onSavePreference(false, it.error.message.toString())

                    showLoading(false)
                    onError(it.error)
                }
                is ExplicitProfileResult.Success -> {
                    tracker.onSavePreference(true)

                    showLoading(false)
                    successSaveShoppingPreference(true)
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModelShared?.userAnswers?.collectLatest {
                onSelectionAnswersChange(it)
            }
        }
    }

    private fun setupViews() {
        pageAdapter = ExplicitProfilePageAdapter(categories, parentFragmentManager, lifecycle)
        viewBinding?.pagerCategories?.apply {
            offscreenPageLimit = categories.data.dataCategories.size + 1
            adapter = pageAdapter
        }

        setupTabCategories()
    }

    private fun setupToolbar() {
        viewBinding?.apply {
            btnBack.setOnClickListener {
                activity?.onBackPressed()
            }

            btnSave.setOnClickListener {
                if (templatesDataModel.isNotEmpty()) {
                    viewModel?.saveShoppingPreferences(templatesDataModel)
                }
            }
        }
    }

    private fun setupTabCategories() {
        viewBinding?.let {
            TabLayoutMediator(it.tabCategories, it.pagerCategories) { tab, position ->
                val tabView = LayoutInflater.from(context).inflate(R.layout.view_item_explicit_profile_tab, it.tabCategories, false)
                tab.customView = setTabCategoryView(position, false, tabView)
            }.attach()

            it.tabCategories.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
                override fun onTabSelected(tab: TabLayout.Tab?) {
                    tab?.customView?.let { tabView ->
                        tracker.clickOnTabMenu(categories.data.dataCategories[tab.position].name)
                        setTabCategoryView(tab.position, true, tabView)
                    }
                }

                override fun onTabUnselected(tab: TabLayout.Tab?) {
                    tab?.customView?.let { tabView ->
                        setTabCategoryView(tab.position, false, tabView)
                    }
                }

                override fun onTabReselected(tab: TabLayout.Tab?) {
                    tab?.customView?.let { tabView ->
                        setTabCategoryView(tab.position, true, tabView)
                    }
                }
            })
        }
    }

    private fun setTabCategoryView(position: Int, isSelected: Boolean, view: View): View {
        viewBinding?.let {
            val tabTitle = view.findViewById<Typography>(R.id.tabTitle)
            val tabIcon = view.findViewById<ImageUnify>(R.id.tabIcon)

            val textColor = if (isSelected) {
                MethodChecker.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_GN500)
            } else {
                MethodChecker.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_NN600)
            }

            val iconUrl = if (isSelected) {
                categories.data.dataCategories[position].imageEnabled
            } else {
                categories.data.dataCategories[position].imageDisabled
            }

            tabTitle.text = categories.data.dataCategories[position].name
            tabTitle.setTextColor(textColor)
            tabIcon.loadImage(iconUrl) {
                useCache(true)
            }
        }
        return view
    }

    private fun showMainView(isShow: Boolean) {
        viewBinding?.apply {
            tabCategories.visibility = if (isShow) View.VISIBLE else View.GONE
            dividerTabLayout.visibility = if (isShow) View.VISIBLE else View.GONE
            pagerCategories.visibility = if (isShow) View.VISIBLE else View.GONE
        }
    }

    private fun showLoading(isLoading: Boolean) {
        viewBinding?.apply {
            loading.visibility = if (isLoading) View.VISIBLE else View.GONE

            showMainView(!isLoading)
        }
    }

    private fun onSuccessGetCategories(categoryDataModel: CategoriesDataModel) {
        categories.data.dataCategories.clear()
        categories = categoryDataModel
        setupViews()
    }

    private fun onSelectionAnswersChange(newTemplateDataModel: TemplateDataModel) {
        val template = templatesDataModel.find {
            it.id == newTemplateDataModel.id
        }

        if (template == null) {
            templatesDataModel.add(newTemplateDataModel)
        } else {
            templatesDataModel.find {
                it.id == newTemplateDataModel.id
            }?.sections = newTemplateDataModel.sections
        }

        viewModelShared?.isAnswersSameWithDefault()?.let {
            viewBinding?.btnSave?.isEnabled = !it
        }
    }

    private fun successSaveShoppingPreference(isSave: Boolean) {
        activity?.let {
            it.setResult(if (isSave) Activity.RESULT_OK else Activity.RESULT_CANCELED)
            it.finish()
        }
    }

    private fun onError(throwable: Throwable) {
        view?.let {
            Toaster.build(it, ErrorHandler.getErrorMessage(context, throwable), Toaster.LENGTH_LONG).show()
        }
    }

    private fun showDialogDiscard() {
        context?.let {
            DialogUnify(it, DialogUnify.HORIZONTAL_ACTION, DialogUnify.NO_IMAGE).apply {
                setTitle(getString(R.string.explicit_profile_back_dialog_title))
                setDescription(getString(R.string.explicit_profile_back_dialog_desc))
                setPrimaryCTAText(getString(R.string.explicit_profile_save))
                setPrimaryCTAClickListener {
                    viewModel?.saveShoppingPreferences(templatesDataModel)
                    this.dismiss()
                }
                setSecondaryCTAText(getString(R.string.explicit_profile_back_dialog_exit))
                setSecondaryCTAClickListener {
                    successSaveShoppingPreference(false)
                    this.dismiss()
                }
            }.show()
        }
    }

    override fun onFragmentBackPressed(): Boolean {
        if (viewModelShared?.isAnswersSameWithDefault() != true) {
            showDialogDiscard()
        } else {
            successSaveShoppingPreference(false)
        }
        return true
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel?.explicitCategories?.removeObservers(this)
        pageAdapter?.clearFragments()
    }

    companion object {
        fun createInstance(): Fragment {
            return ExplicitProfileFragment()
        }
    }
}