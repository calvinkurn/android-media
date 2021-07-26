package com.tokopedia.shop_showcase.shop_showcase_management.presentation.fragment

import android.app.Activity
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.abstraction.common.utils.network.ErrorHandler
import com.tokopedia.design.touchhelper.OnStartDragListener
import com.tokopedia.design.touchhelper.SimpleItemTouchHelperCallback
import com.tokopedia.header.HeaderUnify
import com.tokopedia.shop.common.constant.ShopEtalaseTypeDef.Companion.ETALASE_CUSTOM
import com.tokopedia.shop.common.graphql.data.shopetalase.ShopEtalaseModel
import com.tokopedia.shop_showcase.R
import com.tokopedia.shop_showcase.ShopShowcaseInstance
import com.tokopedia.shop_showcase.common.*
import com.tokopedia.shop_showcase.shop_showcase_management.di.DaggerShopShowcaseManagementComponent
import com.tokopedia.shop_showcase.shop_showcase_management.di.ShopShowcaseManagementComponent
import com.tokopedia.shop_showcase.shop_showcase_management.di.ShopShowcaseManagementModule
import com.tokopedia.shop_showcase.shop_showcase_management.presentation.adapter.ShopShowcaseListReorderAdapterOld
import com.tokopedia.shop_showcase.shop_showcase_management.presentation.viewmodel.ShopShowcaseListViewModel
import com.tokopedia.unifycomponents.LoaderUnify
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import javax.inject.Inject

class ShopShowcaseListReorderFragmentOld : BaseDaggerFragment(),
        ShopShowcaseReorderListener,
        OnStartDragListener,
        HasComponent<ShopShowcaseManagementComponent> {

    companion object {
        const val SHOWCASE_LIST = "SHOWCASE_LIST"

        @JvmStatic
        fun createInstance(shopType: String, showcaseList: ArrayList<ShopEtalaseModel>?, isMyShop: Boolean? = false): ShopShowcaseListReorderFragmentOld {
            val fragment = ShopShowcaseListReorderFragmentOld()
            if (showcaseList != null) {
                val bundle = Bundle()
                bundle.putParcelableArrayList(SHOWCASE_LIST, showcaseList)
                fragment.arguments = bundle
            }
            return fragment
        }
    }

    @Inject
    lateinit var viewModel: ShopShowcaseListViewModel
    lateinit var shopShowcaseFragmentNavigation: ShopShowcaseFragmentNavigation
    private lateinit var headerUnify: HeaderUnify
    private lateinit var headerLayout: CardView
    private lateinit var loading: LoaderUnify
    private lateinit var recyclerView: RecyclerView
    private var layoutManager: LinearLayoutManager? = null
    private var shopShowcaseListReorderAdapter: ShopShowcaseListReorderAdapterOld? = null
    private var shopShowcaseListDefault: ArrayList<ShopEtalaseModel>? = null
    private var itemTouchHelper: ItemTouchHelper? = null
    private var isMyShop: Boolean = false

    override fun getComponent(): ShopShowcaseManagementComponent? {
        return activity?.run {
            DaggerShopShowcaseManagementComponent
                    .builder()
                    .shopShowcaseManagementModule(ShopShowcaseManagementModule(this))
                    .shopShowcaseComponent(ShopShowcaseInstance.getComponent(application))
                    .build()
        }
    }

    override fun getScreenName(): String {
        return ""
    }

    override fun initInjector() {
        component?.inject(this)
    }

    override fun onAttach(activity: Activity) {
        super.onAttach(activity)
        shopShowcaseFragmentNavigation = context as ShopShowcaseFragmentNavigation
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        arguments?.let {
            shopShowcaseListDefault = it.getParcelableArrayList(SHOWCASE_LIST)
            isMyShop = it.getBoolean(ShopShowcaseListParam.EXTRA_IS_MY_SHOP)
        }
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_reorder_showcase_old, container, false)
        headerUnify = view.findViewById(R.id.showcase_list_toolbar)
        headerLayout = view.findViewById(R.id.header_layout)
        recyclerView = view.findViewById(R.id.rv_list_showcase)
        loading = view.findViewById(R.id.loading)
        layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        recyclerView.layoutManager = layoutManager
        shopShowcaseListReorderAdapter = ShopShowcaseListReorderAdapterOld(this, this, isMyShop)
        recyclerView.adapter = shopShowcaseListReorderAdapter
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val itemTouchHelperCallback = SimpleItemTouchHelperCallback(shopShowcaseListReorderAdapter)
        initHeaderUnify()
        initRecyclerView()
        itemTouchHelper = ItemTouchHelper(itemTouchHelperCallback)
        itemTouchHelper!!.attachToRecyclerView(recyclerView)

        showLoading(true)

        showShowcaseData()
        observeReorderShopShowcase()
    }

    override fun onDestroy() {
        viewModel.reoderShopShowcaseResponse.removeObservers(this)
        super.onDestroy()
    }

    override fun onStartDrag(viewHolder: RecyclerView.ViewHolder) {
        itemTouchHelper!!.startDrag(viewHolder)
    }

    private fun initHeaderUnify() {
        headerUnify.apply {
            setNavigationOnClickListener {
                shopShowcaseFragmentNavigation.navigateToPage(
                        page = PageNameConstant.SHOWCASE_LIST_PAGE,
                        tag = null,
                        showcaseList = null)
            }
        }
        headerUnify.actionTextView?.setOnClickListener {
            saveReorderListShowcase()
        }
    }

    private fun showShowcaseData() {
        if (shopShowcaseListDefault != null || shopShowcaseListDefault!!.size > 0) {
            showLoading(false)
            shopShowcaseListReorderAdapter?.updateDataShowcaseList(shopShowcaseListDefault!!)
        }
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            loading.visibility = View.VISIBLE
        } else {
            loading.visibility = View.GONE
        }
    }

    private fun saveReorderListShowcase() {
        val shopShowcaseList = ArrayList<String>()
        shopShowcaseListReorderAdapter?._showcaseList?.let {
            for (shopShowcaseModel in it) {
                if (shopShowcaseModel.type == ETALASE_CUSTOM) {
                    shopShowcaseList.add(shopShowcaseModel.id)
                }
            }
        }
        viewModel.reorderShopShowcaseList(shopShowcaseList)
    }

    private fun observeReorderShopShowcase() {
        viewModel.reoderShopShowcaseResponse.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Success -> {
                    val message = it.data.reorderShopShowcase.message
                    val isSuccess = it.data.reorderShopShowcase.success
                    if (isSuccess) {
                        showSuccessMessage(message)
                        shopShowcaseFragmentNavigation.navigateToPage(PageNameConstant.SHOWCASE_LIST_PAGE, null, null)
                    } else {
                        showErrorResponse(message)
                        shopShowcaseFragmentNavigation.navigateToPage(PageNameConstant.SHOWCASE_LIST_PAGE, null, null)
                    }
                }
                is Fail -> {
                    showErrorMessage(it.throwable)
                }
            }
        })
    }

    private fun initRecyclerView() {
        var currentScrollPosition = 0

        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                currentScrollPosition += dy
                val HAS_ELEVATION = 16.0f
                val NO_ELEVATION = 0f

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    if (currentScrollPosition == 0) {
                        headerLayout?.cardElevation = NO_ELEVATION
                    } else {
                        headerLayout?.cardElevation = HAS_ELEVATION
                    }
                }
            }
        })
    }

    private fun showErrorMessage(t: Throwable) {
        view?.let {
            Toaster.showError(it,
                    ErrorHandler.getErrorMessage(context, t),
                    Snackbar.LENGTH_LONG)
        }
    }

    private fun showErrorResponse(message: String) {
        view?.let {
            Toaster.showError(it, message, Snackbar.LENGTH_LONG)
        }
    }

    private fun showSuccessMessage(message: String) {
        view?.let {
            Toaster.showNormal(it, message, Snackbar.LENGTH_LONG)
        }
    }

}