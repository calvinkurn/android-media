package com.rahullohra.fakeresponse.presentaiton.fragments

import android.os.Bundle
import android.view.View
import android.widget.Toast
import android.widget.ViewFlipper
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.rahullohra.fakeresponse.R
import com.rahullohra.fakeresponse.ResponseListData
import com.rahullohra.fakeresponse.data.diProvider.fragments.GqlFragmentProvider
import com.rahullohra.fakeresponse.presentaiton.adapters.GqlRvAdapter
import com.rahullohra.fakeresponse.presentaiton.livedata.Fail
import com.rahullohra.fakeresponse.presentaiton.livedata.Loading
import com.rahullohra.fakeresponse.presentaiton.livedata.Success
import com.rahullohra.fakeresponse.presentaiton.viewmodels.FakeResponseModel


class GqlFragment : BaseFragment() {
    val STATE_EMPTY = 0
    val STATE_DATA = 1

    companion object {
        fun newInstance(): GqlFragment {
            return GqlFragment()
        }
    }

    lateinit var rv: RecyclerView
    lateinit var viewFlipper: ViewFlipper

    lateinit var adapter: GqlRvAdapter
    lateinit var dataList: ArrayList<ResponseListData>
    lateinit var mapOfData: MutableMap<Int, ResponseListData>

    lateinit var viewModel: FakeResponseModel

    override fun getLayout() = R.layout.fragment_gql

    override fun initVars() {
        dataList = ArrayList()
        adapter = GqlRvAdapter(dataList, itemClickCallback = { gqlId, isChecked ->
            viewModel.toggleGql(gqlId, isChecked)
        })
    }

    override fun initViews(view: View) {
        viewFlipper = view.findViewById(R.id.viewFlipper)
        rv = view.findViewById(R.id.rv)
        rv.layoutManager = LinearLayoutManager(context)

        rv.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
        rv.adapter = adapter

        mapOfData = mutableMapOf()
    }

    override fun setupFragment() {
        injectComponents()
        setListeners()
        getData()
    }

    fun setListeners() {
        viewModel.liveData.observe(viewLifecycleOwner, Observer { it ->
            when (it) {
                is Success -> {
                    if (it.data.isNotEmpty()) {
                        mapOfData.clear()
                        dataList.clear()
                        dataList.addAll(it.data)
                        dataList.forEachIndexed { index, item -> mapOfData[item.id] = item }
                        adapter.notifyDataSetChanged()
                        showDataUi()
                    } else {
                        showEmptyUi()
                    }

                }
                is Fail -> {
                }
                is Loading -> {
                }
            }
        })

        viewModel.toggleLiveData.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Success -> toggleGql(it.data.first, it.data.second)
                is Fail -> showErrorToggle()
            }
        })
    }

    fun toggleGql(id: Int, enable: Boolean) {
        mapOfData[id]?.isChecked = enable
        adapter.notifyDataSetChanged()
    }

    fun showErrorToggle() {
        Toast.makeText(context, "Unable to toggle", Toast.LENGTH_SHORT).show()
    }

    fun showEmptyUi() {
        viewFlipper.displayedChild = STATE_EMPTY
    }

    fun showDataUi() {
        viewFlipper.displayedChild = STATE_DATA
    }

    private fun getData() {
        viewModel.getGql()
    }

    private fun injectComponents() {
        GqlFragmentProvider().inject(this)
    }

    override fun onResume() {
        super.onResume()
        getData()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        injectComponents()
    }
}