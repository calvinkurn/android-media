package com.tokopedia.checkout.view.feature.cornerlist

import android.os.Bundle
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast

import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.base.view.recyclerview.EndlessRecyclerViewScrollListener
import com.tokopedia.checkout.R
import com.tokopedia.checkout.domain.datamodel.addressoptions.CornerAddressModel
import com.tokopedia.checkout.view.di.component.CartComponent
import com.tokopedia.checkout.view.di.module.ShipmentAddressListModule
import com.tokopedia.checkout.view.di.module.TrackingAnalyticsModule
import com.tokopedia.design.text.SearchInputView
import com.tokopedia.shipping_recommendation.domain.shipping.RecipientAddressModel
import com.tokopedia.checkout.view.di.component.DaggerShipmentAddressListComponent

import java.util.ArrayList

import javax.inject.Inject

/**
 * Created by fajarnuha on 09/02/19.
 */
class CornerListFragment : BaseDaggerFragment(), CornerContract.View, CornerAdapter.OnItemClickListener {

    private var mBranchList: MutableList<RecipientAddressModel> = ArrayList()
    private lateinit var mListener: BranchChosenListener
    private val mAdapter = CornerAdapter(mBranchList, this)

    private lateinit var mEmptyView: View
    private lateinit var mSearchView: SearchInputView
    private lateinit var mRvCorner: RecyclerView
    private lateinit var mProgressBar: ProgressBar
    private var mScrollListener: EndlessRecyclerViewScrollListener? = null

    @Inject
    lateinit var mPresenter: CornerListPresenter

    override fun initInjector() {
        val component = DaggerShipmentAddressListComponent.builder()
                .cartComponent(getComponent<CartComponent>(CartComponent::class.java))
                .shipmentAddressListModule(ShipmentAddressListModule(activity))
                .trackingAnalyticsModule(TrackingAnalyticsModule())
                .build()
        component.inject(this)
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

        mRvCorner.setHasFixedSize(true)
        val layoutManager = LinearLayoutManager(context)
        mRvCorner.layoutManager = layoutManager
        mRvCorner.addItemDecoration(DividerItemDecoration(context!!, DividerItemDecoration.VERTICAL))
        mScrollListener = object : EndlessRecyclerViewScrollListener(layoutManager) {
            override fun onLoadMore(page: Int, totalItemsCount: Int) {
                mPresenter.loadMore(page + 1)
            }
        }
        mRvCorner.addOnScrollListener(mScrollListener)
        mRvCorner.adapter = mAdapter

        mSearchView.setSearchHint(activity!!.getString(R.string.hint_search_corner))
        mSearchView.setListener(object : SearchInputView.Listener {
            override fun onSearchSubmitted(text: String) {
                mPresenter.searchQuery(text)
            }

            override fun onSearchTextChanged(text: String) {

            }
        })

        mPresenter.attachView(this)
        mPresenter.getData()
    }

    override fun onItemClick(corner: RecipientAddressModel) {
        mListener.onCornerChosen(corner)
    }

    override fun showData(data: List<RecipientAddressModel>) {
        mAdapter.setAddress(data)
        mScrollListener?.resetState()
        mEmptyView.visibility = View.GONE
    }

    override fun appendData(data: List<RecipientAddressModel>) {
        mAdapter.appendAddress(data)
        mScrollListener?.updateStateAfterGetData()
    }

    override fun notifyHasNotNextPage() {
        mScrollListener?.setHasNextPage(false)
    }

    override fun setLoadingState(active: Boolean) {
        mProgressBar.visibility = if (active) View.VISIBLE else View.GONE
    }

    override fun showError(e: Throwable) {
        Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
    }

    override fun showEmptyView() {
        mEmptyView.visibility = View.VISIBLE
        mRvCorner.visibility = View.GONE
    }

    fun setCornerListener(listener: BranchChosenListener) {
        mListener = listener
    }

    interface BranchChosenListener {
        fun onCornerChosen(corner: RecipientAddressModel)
    }

    companion object {

        private val ARGUMENTS_BRANCH_LIST = "ARGUMENTS_BRANCH_LIST"

        fun newInstance(): CornerListFragment {
            return CornerListFragment()
        }

        fun newInstance(modelList: List<CornerAddressModel>): CornerListFragment {
            val args = Bundle()
            args.putParcelableArrayList(ARGUMENTS_BRANCH_LIST, ArrayList(modelList))
            val fragment = CornerListFragment()
            fragment.arguments = args
            return fragment
        }
    }

}
