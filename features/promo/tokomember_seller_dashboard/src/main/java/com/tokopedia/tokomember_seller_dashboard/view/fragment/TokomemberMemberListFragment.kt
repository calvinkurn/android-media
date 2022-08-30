package com.tokopedia.tokomember_seller_dashboard.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.tokomember_seller_dashboard.R
import com.tokopedia.tokomember_seller_dashboard.view.adapter.TmMemberListAdapter
import com.tokopedia.tokomember_seller_dashboard.view.adapter.decoration.TmMemberItemDecoration
import com.tokopedia.tokomember_seller_dashboard.view.viewmodel.TmMemberListViewModel
import kotlinx.android.synthetic.main.tm_member_list_fragment.*

class TokomemberMemberListFragment : Fragment() {

    private lateinit var tmMemberListRv : RecyclerView


    private val tmMemberViewModel:TmMemberListViewModel by lazy {
        ViewModelProvider(this).get(TmMemberListViewModel::class.java)
    }

    private val tmMemberAdapter : TmMemberListAdapter by lazy {
        TmMemberListAdapter(tmMemberViewModel.memberList,requireContext())
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
        tmMemberListRv = view.findViewById(R.id.tm_member_list_recycler_view)
        initUI()
    }

    private fun initUI(){
        setupHeader()
        initialSetupMemberList()
    }

    private fun initialSetupMemberList(){
        tmMemberListRv.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = tmMemberAdapter
            val dividerDecor  = TmMemberItemDecoration(requireContext())
            addItemDecoration(dividerDecor)
        }
    }

    private fun setupHeader(){
        tm_member_list_header.setNavigationOnClickListener{
            activity?.onBackPressed()
        }
    }





    companion object{
        fun getInstance() : TokomemberMemberListFragment{
            return TokomemberMemberListFragment()
        }

        private val layout = R.layout.tm_member_list_fragment
    }
}