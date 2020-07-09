package com.tokopedia.manageaddress.ui.cornerlist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.base.view.recyclerview.EndlessRecyclerViewScrollListener
import com.tokopedia.abstraction.common.utils.network.ErrorHandler
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper
import com.tokopedia.design.text.SearchInputView
import com.tokopedia.logisticdata.data.entity.address.RecipientAddressModel
import com.tokopedia.manageaddress.R
import java.util.*
import javax.inject.Inject

/**
 * Created by fajarnuha on 09/02/19.
 */
class CornerListFragment : BaseDaggerFragment(), CornerContract.View, CornerAdapter.OnItemClickListener {

    private var mBranchList: MutableList<RecipientAddressModel> = ArrayList()
    private val mAdapter = CornerAdapter(mBranchList, this)
    private lateinit var mListener: ActionListener
    private val mLayoutManager: RecyclerView.LayoutManager = LinearLayoutManager(context)
    private var mScrollListener: EndlessRecyclerViewScrollListener = object : EndlessRecyclerViewScrollListener(mLayoutManager) {
        override fun onLoadMore(page: Int, totalItemsCount: Int) {
            mPresenter.loadMore(page + 1)
        }
    }

    private lateinit var mEmptyView: View
    private lateinit var mSearchView: SearchInputView
    private lateinit var mRvCorner: RecyclerView
    private lateinit var mProgressBar: ProgressBar
    private lateinit var mErrorView: View

    @Inject
    lateinit var mPresenter: CornerListPresenter

    override fun initInjector() {
        activity?.let {
            val baseAppComponent = it.application
            if (baseAppComponent is BaseMainApplication) {
                DaggerCornerComponent.builder()
                        .baseAppComponent(baseAppComponent.baseAppComponent)
                        .build()
                        .inject(this)
            }
        }
    }

    override fun getScreenName(): String {
        return "Pilih Lokasi Tokopedia Corner"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            mBranchList = it.getParcelableArrayList(ARGUMENTS_BRANCH_LIST) ?: ArrayList()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.bottom_sheet_corner, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        mSearchView = view.findViewById(R.id.sv_address_search_box)
        mEmptyView = view.findViewById(R.id.ll_no_result)
        mRvCorner = view.findViewById(R.id.rv_corner_list)
        mProgressBar = view.findViewById(R.id.progress_bar)
        mErrorView = view.findViewById(R.id.errorview)

        mRvCorner.setHasFixedSize(true)
        mRvCorner.layoutManager = mLayoutManager
        mRvCorner.addItemDecoration(DividerItemDecoration(context!!, DividerItemDecoration.VERTICAL))
        mRvCorner.addOnScrollListener(mScrollListener)
        mRvCorner.adapter = mAdapter

        mSearchView.setSearchHint(activity!!.getString(R.string.hint_search_corner))
        mSearchView.setListener(object : SearchInputView.Listener {
            override fun onSearchSubmitted(text: String) {
                mPresenter.getList(text)
            }

            override fun onSearchTextChanged(text: String) {

            }
        })

        mPresenter.attachView(this)
        mPresenter.getList("")
    }

    override fun onItemClick(corner: RecipientAddressModel) {
        mListener.onCornerChosen(corner)
    }

    override fun showData(data: List<RecipientAddressModel>) {
        mAdapter.setAddress(data)
        mScrollListener.resetState()
        mRvCorner.visibility = View.VISIBLE
        mEmptyView.visibility = View.GONE
        mErrorView.visibility = View.GONE
    }

    override fun appendData(data: List<RecipientAddressModel>) {
        mAdapter.appendAddress(data)
        mScrollListener.updateStateAfterGetData()
    }

    override fun notifyHasNotNextPage() {
        mScrollListener.setHasNextPage(false)
    }

    override fun setLoadingState(active: Boolean) {
        mProgressBar.visibility = if (active) View.VISIBLE else View.INVISIBLE
    }

    override fun showError(e: Throwable) {
        mErrorView.visibility = View.VISIBLE
        mRvCorner.visibility = View.GONE
        mEmptyView.visibility = View.GONE
        context?.let {
            NetworkErrorHelper.showEmptyState(it, mErrorView, ErrorHandler.getErrorMessage(it, e)) {
                mPresenter.getList("")
            }
        }
    }

    override fun showEmptyView() {
        mEmptyView.visibility = View.VISIBLE
        mErrorView.visibility = View.GONE
        mRvCorner.visibility = View.GONE
    }

    fun setCornerListener(listener: ActionListener) {
        mListener = listener
    }

    interface ActionListener {
        fun onCornerChosen(corner: RecipientAddressModel)
    }

    companion object {

        private const val ARGUMENTS_BRANCH_LIST = "ARGUMENTS_BRANCH_LIST"

        @JvmStatic
        fun newInstance(): CornerListFragment {
            return CornerListFragment()
        }

    }

}
