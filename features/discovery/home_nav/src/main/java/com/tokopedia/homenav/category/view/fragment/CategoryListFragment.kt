package com.tokopedia.homenav.category.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.applink.RouteManager
import com.tokopedia.homenav.R
import com.tokopedia.homenav.base.diffutil.HomeNavListener
import com.tokopedia.homenav.base.datamodel.HomeNavMenuDataModel
import com.tokopedia.homenav.category.analytics.CategoryTracking
import com.tokopedia.homenav.category.view.adapter.CategoryListAdapter
import com.tokopedia.homenav.category.view.adapter.typefactory.CategoryListTypeFactory
import com.tokopedia.homenav.category.view.adapter.typefactory.CategoryListTypeFactoryImpl
import com.tokopedia.homenav.category.view.di.DaggerCategoryListComponent
import com.tokopedia.homenav.di.DaggerBaseNavComponent
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.searchbar.navigation_component.NavToolbar
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.android.synthetic.main.fragment_nav_category.view.*
import javax.inject.Inject

class CategoryListFragment: BaseDaggerFragment(), HomeNavListener {
    @Inject
    lateinit var userSessionInterface: UserSessionInterface

    private val typeFactory: CategoryListTypeFactory = CategoryListTypeFactoryImpl(this)
    private val adapter: CategoryListAdapter = CategoryListAdapter(typeFactory)
    private lateinit var menuDataModel: HomeNavMenuDataModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val pageTitle = arguments?.getString("title", "")?:""
        activity?.findViewById<NavToolbar>(R.id.toolbar)?.let {
            it.setToolbarTitle(pageTitle)
            it.setBackButtonType(NavToolbar.Companion.BackType.BACK_TYPE_BACK)
        }
        return inflater.inflate(R.layout.fragment_nav_category, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerView(view)
        arguments?.get(BUNDLE_MENU_ITEM)?.let {
            menuDataModel = it as HomeNavMenuDataModel
            adapter.submitList(it.submenus)
            if(it.submenus.isEmpty()){
                showGlobalError(view)
            }
        }
    }

    override fun getScreenName(): String {
        return PAGE_NAME
    }

    override fun initInjector() {
        val baseNavComponent
                = DaggerBaseNavComponent.builder()
                .baseAppComponent((requireActivity().applicationContext as BaseMainApplication).baseAppComponent)
                .build() as DaggerBaseNavComponent
        DaggerCategoryListComponent.builder().baseNavComponent(baseNavComponent).build().inject(this)
    }

    override fun onRefresh() {}

    override fun onMenuClick(homeNavMenuDataModel: HomeNavMenuDataModel) {
        arguments?.getString(TITLE_ARGS, "")?.let {
            if(!it.contains(OTHER)){
                CategoryTracking.onClickItem(homeNavMenuDataModel.id.toString(), userSessionInterface.userId)
            } else {
                CategoryTracking.onClickLainnyaItem(homeNavMenuDataModel.itemTitle, userSessionInterface.userId)
            }
            RouteManager.route(context, homeNavMenuDataModel.applink)
        }
    }

    override fun onMenuImpression(homeNavMenuDataModel: HomeNavMenuDataModel) {
    }

    override fun getUserId(): String {
        return userSessionInterface.userId
    }

    override fun getReviewCounterAbIsUnify(): Boolean {
        // default red notif unify
        return true
    }

    private fun initRecyclerView(view: View) {
        view.recycler_view?.adapter = adapter
    }

    private fun showGlobalError(view: View){
        view.category_global_error?.let {globalError ->
            globalError.errorSecondaryAction.hide()
            globalError.show()
            globalError.findViewById<UnifyButton>(R.id.globalerrors_action)?.text = getString(R.string.category_back_menu)
            globalError.setActionClickListener {
                activity?.let { activity ->
                    Navigation.findNavController(activity, R.id.fragment_container).navigateUp()
                }
            }
        }
    }

    override fun onDestroy() {
        arguments?.getString(TITLE_ARGS, "")?.let {
            if(!it.contains(OTHER)){
                CategoryTracking.onClickCloseCategory(userSessionInterface.userId)
            } else {
                CategoryTracking.onClickCloseLainnya(userSessionInterface.userId)
            }
        }
        super.onDestroy()
    }

    companion object {
        const val PAGE_NAME = "CATEGORY_LIST"
        const val OTHER = "lainnya"
        private const val TITLE_ARGS = "title"
        private const val BUNDLE_MENU_ITEM = "menu_item_bundle"
    }

}