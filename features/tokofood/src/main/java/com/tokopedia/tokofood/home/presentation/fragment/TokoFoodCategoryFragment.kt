package com.tokopedia.tokofood.home.presentation.fragment

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseMultiFragActivity
import com.tokopedia.abstraction.base.view.activity.BaseToolbarActivity
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.base.view.fragment.IBaseMultiFragment
import com.tokopedia.applink.internal.ApplinkConsInternalNavigation
import com.tokopedia.kotlin.extensions.view.observe
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.localizationchooseaddress.domain.model.LocalCacheModel
import com.tokopedia.searchbar.navigation_component.NavToolbar
import com.tokopedia.searchbar.navigation_component.icons.IconBuilder
import com.tokopedia.searchbar.navigation_component.icons.IconBuilderFlag
import com.tokopedia.searchbar.navigation_component.icons.IconList
import com.tokopedia.tokofood.R
import com.tokopedia.tokofood.common.util.Constant
import com.tokopedia.tokofood.databinding.FragmentTokofoodCategoryBinding
import com.tokopedia.tokofood.home.di.DaggerTokoFoodHomeComponent
import com.tokopedia.tokofood.home.domain.constanta.TokoFoodLayoutState
import com.tokopedia.tokofood.home.presentation.adapter.CustomLinearLayoutManager
import com.tokopedia.tokofood.home.presentation.adapter.TokoFoodCategoryAdapter
import com.tokopedia.tokofood.home.presentation.adapter.TokoFoodCategoryAdapterTypeFactory
import com.tokopedia.tokofood.home.presentation.adapter.TokoFoodListDiffer
import com.tokopedia.tokofood.home.presentation.uimodel.TokoFoodListUiModel
import com.tokopedia.tokofood.home.presentation.view.listener.TokoFoodView
import com.tokopedia.tokofood.home.presentation.viewmodel.TokoFoodCategoryViewModel
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.utils.lifecycle.autoClearedNullable
import javax.inject.Inject

class TokoFoodCategoryFragment: BaseDaggerFragment(),
    IBaseMultiFragment,
    TokoFoodView{

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private var binding by autoClearedNullable<FragmentTokofoodCategoryBinding>()
    private val viewModel by lazy {
        ViewModelProvider(this, viewModelFactory).get(TokoFoodCategoryViewModel::class.java)
    }

    private val adapter by lazy {
        TokoFoodCategoryAdapter(
            typeFactory = TokoFoodCategoryAdapterTypeFactory(
                this
            ),
            differ = TokoFoodListDiffer()
        )
    }

    companion object {
        private const val PAGE_TITLE_PARAM = "pageTitle"
        private const val OPTION_PARAM = "option"
        private const val CUISINE_PARAM = "cuisine"
        private const val SORT_BY_PARAM = "sortBy"

        fun createInstance(): TokoFoodCategoryFragment {
            return TokoFoodCategoryFragment()
        }
    }

    private var pageTitle: String = ""
    private var option: Int = 0
    private var sortBy: Int = 0
    private var cuisine: String = ""
    private var rvCategory: RecyclerView? = null
    private var swipeLayout: SwipeRefreshLayout? = null
    private var navToolbar: NavToolbar? = null
    private var rvLayoutManager: CustomLinearLayoutManager? = null
    private var localCacheModel: LocalCacheModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val bundle = arguments?.getString(Constant.DATA_KEY) ?: ""
        val uri = Uri.parse(bundle)
        pageTitle = uri.getQueryParameter(PAGE_TITLE_PARAM) ?: ""
        option = uri.getQueryParameter(OPTION_PARAM).toIntOrZero()
        cuisine = uri.getQueryParameter(CUISINE_PARAM) ?: ""
        sortBy = uri.getQueryParameter(SORT_BY_PARAM).toIntOrZero()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTokofoodCategoryBinding.inflate(inflater)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUi()
        setupNavToolbar()
        setupRecycleView()
        observeLiveData()

        loadLayout()
    }

    override fun getFragmentTitle(): String? {
        return ""
    }

    override fun getFragmentToolbar(): Toolbar? {
        return null
    }

    override fun getScreenName(): String {
        return ""
    }

    override fun initInjector() {
        activity?.let {
            DaggerTokoFoodHomeComponent
                .builder()
                .baseAppComponent((it.applicationContext as BaseMainApplication).baseAppComponent)
                .build()
                .inject(this)
        }
    }

    override fun navigateToNewFragment(fragment: Fragment) {
        (activity as? BaseMultiFragActivity)?.navigateToNewFragment(fragment)
    }

    override fun getFragmentManagerPage(): FragmentManager = childFragmentManager

    override fun getFragmentPage(): Fragment = this

    override fun refreshLayoutPage() = onRefreshLayout()

    private fun onRefreshLayout() {
        //TODO Refresh layout
    }

    private fun observeLiveData() {
        observe(viewModel.layoutList) {
            when (it) {
                is Success -> onSuccessGetHomeLayout(it.data)
            }
        }
    }

    private fun setupUi() {
        view?.apply {
            rvCategory = binding?.rvCategory
            navToolbar = binding?.navToolbar
            swipeLayout = binding?.swipeRefreshLayout
        }
    }

    private fun setupRecycleView() {
        context?.let {
            rvCategory?.apply {
                adapter = this@TokoFoodCategoryFragment.adapter
                rvLayoutManager = CustomLinearLayoutManager(it)
                layoutManager = rvLayoutManager
            }
        }
    }

    private fun setupNavToolbar() {
        setupTopNavigation()
        setIconNavigation()
    }

    private fun setupTopNavigation() {
        navToolbar?.let { toolbar ->
            viewLifecycleOwner.lifecycle.addObserver(toolbar)
            activity?.let {
                toolbar.showShadow(true)
                toolbar.setupToolbarWithStatusBar(it, applyPadding = false, applyPaddingNegative = true)
                toolbar.setToolbarTitle(pageTitle)
            }
        }
    }

    private fun setIconNavigation() {
        val icons =
            IconBuilder(IconBuilderFlag(pageSource = ApplinkConsInternalNavigation.SOURCE_HOME))
        navToolbar?.setIcon(icons)
    }

    private fun onSuccessGetHomeLayout(data: TokoFoodListUiModel) {
        when (data.state) {
            TokoFoodLayoutState.SHOW -> onShowHomeLayout(data)
            TokoFoodLayoutState.HIDE -> onHideHomeLayout(data)
            TokoFoodLayoutState.LOADING -> onLoadingHomelayout(data)
            else -> showHomeLayout(data)
        }
    }

    private fun onShowHomeLayout(data: TokoFoodListUiModel) {
        showHomeLayout(data)
    }

    private fun onHideHomeLayout(data: TokoFoodListUiModel) {
        showHomeLayout(data)
    }

    private fun onLoadingHomelayout(data: TokoFoodListUiModel) {
        showHomeLayout(data)
    }

    private fun showHomeLayout(data: TokoFoodListUiModel) {
        rvCategory?.post {
            adapter.submitList(data.items)
        }
    }

    private fun loadLayout() {
        viewModel.getLoadingState()
    }

}