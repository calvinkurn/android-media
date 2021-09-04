package com.tokopedia.createpost.view.plist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.createpost.createpost.R
import com.tokopedia.library.baseadapter.AdapterCallback
import kotlinx.android.synthetic.main.fragment_shop_plist_page.view.*
import androidx.recyclerview.widget.GridLayoutManager
import com.tokopedia.iconunify.getIconUnifyDrawable
import com.tokopedia.unifycomponents.ChipsUnify


class ShopProductListFragment : BaseDaggerFragment(), View.OnClickListener, AdapterCallback {

//    private var mItemDecoration: SpacesItemDecoration? = null

    val presenter: ShopPageProductListViewModel by lazy { ViewModelProviders.of(this)[ShopPageProductListViewModel::class.java] }

    private val mAdapter: ShopProductListBaseAdapter by lazy {
        ShopProductListBaseAdapter(
            presenter,
            this
        )
    }

//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_shop_plist_page, container, false)
        initViews(view)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initListener()
    }

//    override fun onResume() {
//        super.onResume()
//    }

//////    override fun getAppContext(): Context? {
//////        return context
//////    }
//////
//    override fun showLoader() {
//        container?.displayedChild = CONTAINER_LOADER
//        swipe_refresh_layout?.isRefreshing = false
//    }
//////
//    override fun hideLoader() {
//        container?.displayedChild = CONTAINER_DATA
//        swipe_refresh_layout?.isRefreshing = false
//    }
////
////    override fun getActivityContext(): Context? {
////        return activity
////    }
//
//    override fun getScreenName(): String {
//        return AnalyticsTrackerUtil.ScreenKeys.MY_COUPON_LISTING_SCREEN_NAME
//    }

    override fun initInjector() {
//        DaggerTokopointBundleComponent.builder()
//                .baseAppComponent((activity?.application as BaseMainApplication).baseAppComponent)
//                .tokopointsQueryModule(TokopointsQueryModule(requireActivity()))
//                .build().inject(this)
    }

    override fun onClick(source: View) {
//        if (source.id == R.id.text_failed_action) {
//            showLoader()
//            mAdapter.loadData(mAdapter.currentPageIndex)
//        }
    }

    private fun initViews(view: View) {
//        mItemDecoration = SpacesItemDecoration(0,
//                activityContext!!.resources.getDimensionPixelOffset(com.tokopedia.unifyprinciples.R.dimen.unify_space_0),
//                activityContext!!.resources.getDimensionPixelOffset(com.tokopedia.unifyprinciples.R.dimen.unify_space_0))
//        if (view.recycler_view_coupons.itemDecorationCount > 0) {
//            view.recycler_view_coupons.removeItemDecoration(mItemDecoration!!)
//        }
//        view.recycler_view_coupons.addItemDecoration(mItemDecoration!!)
        view.recycler_view.layoutManager = GridLayoutManager(activity, 2)
        view.recycler_view.adapter = mAdapter

        mAdapter.resetAdapter()
        mAdapter.notifyDataSetChanged()
        mAdapter.startDataLoading()

        view.cu_sort_chip.chip_right_icon.background = (getIconUnifyDrawable(
            requireContext(),
            R.drawable.ic_arrow_down,
            ContextCompat.getColor(requireContext(), R.color.black_70)
        ))

        view.cu_sort_chip.setOnClickListener {
            val getImeiBS = ShopPListSortFilterBs.newInstance(presenter)
            fragmentManager?.let { fm -> getImeiBS.show(fm, "") }
        }
    }

    private fun initListener() {
        if (view == null) {
            return
        }

        addListObserver()
        addSortValObserver()
    }

    private fun addListObserver() = presenter.productList.observe(this, Observer {
        it?.let {
            when (it) {
                is Loading -> {
                    mAdapter.resetAdapter()
                    mAdapter.notifyDataSetChanged()
                    mAdapter.startDataLoading()
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
    }
    )

//    override fun emptyCoupons(errors: Map<String, String>?) {
//        if (view == null || errors == null) {
//            return
//        }
//        view!!.findViewById<View>(R.id.button_continue).visibility = View.VISIBLE
//
//        container.displayedChild = CONTAINER_EMPTY
//    }

    override fun onRetryPageLoad(pageNumber: Int) {}

    override fun onEmptyList(rawObject: Any) {
        //hideLoader()
        //emptyCoupons((rawObject as TokoPointPromosEntity).coupon.emptyMessage)
    }

    override fun onStartFirstPageLoad() {
        //  showLoader()
    }

    override fun onFinishFirstPageLoad(count: Int, rawObject: Any?) {
        //view!!.postDelayed({ hideLoader() }, 250)
    }

    override fun onStartPageLoad(pageNumber: Int) {}

    override fun onFinishPageLoad(itemCount: Int, pageNumber: Int, rawObject: Any?) {
//        swipe_refresh_layout.isRefreshing = false
    }

    override fun onError(pageNumber: Int) {
        if (pageNumber == 1) {
//            container.displayedChild = CONTAINER_ERROR
//            server_error_view?.showErrorUi(NetworkDetector.isConnectedToInternet(appContext))
        }
//        swipe_refresh_layout.isRefreshing = false
    }

    override fun onDestroyView() {
        //mAdapter.onDestroyView()
        super.onDestroyView()
    }

    override fun getScreenName(): String {
        // TODO("Not yet implemented")
        return ""
    }

    companion object {
        private val CONTAINER_LOADER = 0
        private val CONTAINER_DATA = 1
        private val CONTAINER_ERROR = 2
        private val CONTAINER_EMPTY = 3
        val REQUEST_CODE_STACKED_IN_ADAPTER = 4
        val REQUEST_CODE_STACKED_ADAPTER = 5

        fun newInstance(): ShopProductListFragment {
            return ShopProductListFragment()
        }
    }
}
