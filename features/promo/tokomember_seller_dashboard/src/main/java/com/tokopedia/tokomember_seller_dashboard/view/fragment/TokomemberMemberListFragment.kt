package com.tokopedia.tokomember_seller_dashboard.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ViewFlipper
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.kotlin.extensions.orFalse
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.kotlin.extensions.view.toZeroIfNull
import com.tokopedia.tokomember_seller_dashboard.R
import com.tokopedia.tokomember_seller_dashboard.di.component.DaggerTokomemberDashComponent
import com.tokopedia.tokomember_seller_dashboard.model.DataModel
import com.tokopedia.tokomember_seller_dashboard.tracker.TmTracker
import com.tokopedia.tokomember_seller_dashboard.util.InfiniteListResult
import com.tokopedia.tokomember_seller_dashboard.util.TokoLiveDataResult
import com.tokopedia.tokomember_seller_dashboard.view.adapter.TmMemberListAdapter
import com.tokopedia.tokomember_seller_dashboard.view.adapter.decoration.TmMemberItemDecoration
import com.tokopedia.tokomember_seller_dashboard.view.viewmodel.TmMemberListViewModel
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.android.synthetic.main.tm_member_list_fragment.*
import javax.inject.Inject

class TokomemberMemberListFragment : BaseDaggerFragment() {

    private var tmMemberListRv : RecyclerView? = null
    private var flipper: ViewFlipper? = null
    private var shopId:Int? = null

    @Inject
    lateinit var userSessionInterface : UserSessionInterface

    @Inject
    lateinit var tmTracker: TmTracker

    @Inject
    lateinit var viewModelFactory: dagger.Lazy<ViewModelProvider.Factory>

    private val tmMemberViewModel:TmMemberListViewModel by lazy {
        ViewModelProvider(this,viewModelFactory.get()).get(TmMemberListViewModel::class.java)
    }

    private var tmMemberAdapter : TmMemberListAdapter?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        shopId = userSessionInterface.shopId.toIntOrZero()
    }

    override fun initInjector() {
        DaggerTokomemberDashComponent.builder().baseAppComponent((activity?.application as BaseMainApplication).baseAppComponent).build().inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(layout,container,false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUI()
        observeViewModel()
        shopId?.let {
            tmMemberViewModel.getInitialMemberList(it)
            tmTracker.viewMemberList(it.toString())
        }
    }

    override fun getScreenName() = ""

    private fun initUI(){
        initViews()
    }


    private fun observeViewModel(){
        tmMemberViewModel.tmMemberListInitialResult.observe(viewLifecycleOwner){
            when(it.status){
                TokoLiveDataResult.STATUS.LOADING -> {
                    flipper?.displayedChild  = 0
                }
                TokoLiveDataResult.STATUS.SUCCESS -> {
                    flipper?.displayedChild  = 1
                    setupHeader()
                    setupRecyclerView()
                    tmMemberViewModel.mapInitialMemberList()
                }
                else -> {
                    flipper?.displayedChild  = 0
                }
            }
        }

        tmMemberViewModel.tmMemberList.observe(viewLifecycleOwner){
            updateMemberList(it)
        }
    }

    private fun setupRecyclerView(){
        tmMemberListRv?.apply {
            tmMemberAdapter = TmMemberListAdapter(listOf(),context)
            layoutManager = LinearLayoutManager(context)
            val dividerDecor  = TmMemberItemDecoration(context)
            addItemDecoration(dividerDecor)
            this.adapter = tmMemberAdapter
            setHasFixedSize(true)
            addRvScrollListener()
        }
    }

    private fun addRvScrollListener(){
        tmMemberListRv?.addOnScrollListener(object : RecyclerView.OnScrollListener(){

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if(tmMemberViewModel.tmMemberList.value?.status == InfiniteListResult.InfiniteState.LOADING) return
                val linearManager = recyclerView.layoutManager as LinearLayoutManager
                val lastVisiblePos = linearManager.findLastVisibleItemPosition()
                val totalCount = recyclerView.adapter?.itemCount.toZeroIfNull()
                val hasNext = tmMemberViewModel.hasMorePages.orFalse()
                if(lastVisiblePos!=RecyclerView.NO_POSITION && totalCount - lastVisiblePos <= 4 && hasNext){
                   if(shopId!=null) tmMemberViewModel.getMoreMembers(shopId!!)
                }
                else if(!hasNext) removeRvScrollListener()
            }

        })
    }

    private fun removeRvScrollListener(){
        tmMemberListRv?.clearOnScrollListeners()
    }

    private fun updateMemberList(result:InfiniteListResult<DataModel>){
        tmMemberAdapter?.apply {
            result.list?.let { it1 ->
                val previousItemCount = itemCount.toZeroIfNull()
                updateList(it1)
                val currentItemCount = it1.size
                when(result.status){
                    InfiniteListResult.InfiniteState.LOADING -> notifyItemInserted(itemCount-1)
                    InfiniteListResult.InfiniteState.JUST_STOPPED -> notifyItemRemoved(itemCount-1)
                    else -> notifyItemRangeInserted(previousItemCount,currentItemCount-previousItemCount)

                }
            }
        }
    }

    private fun setupHeader(){
        tm_member_list_header.apply {
            setNavigationOnClickListener{
                this@TokomemberMemberListFragment.activity?.onBackPressed()
            }
            val memberCount = tmMemberViewModel.tmMemberListInitialResult.value
                              ?.data?.membershipGetUserCardMemberList?.userCardMemberList
                              ?.sumUserCardMember?.sumUserCardMember?.toZeroIfNull().toString()
            title = context?.resources?.getString(R.string.tm_member_list_header_title,memberCount).orEmpty()
        }
    }

    private fun initViews(){
        view?.let{
            flipper = it.findViewById(R.id.member_list_flipper)
            tmMemberListRv = it.findViewById(R.id.tm_member_list_recycler_view)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        removeRvScrollListener()
    }



    companion object{
        fun getInstance() : TokomemberMemberListFragment = TokomemberMemberListFragment()

        private val layout = R.layout.tm_member_list_fragment
    }
}
