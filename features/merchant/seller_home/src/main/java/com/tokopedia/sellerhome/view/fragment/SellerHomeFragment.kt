package com.tokopedia.sellerhome.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.fragment.BaseListFragment
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.sellerhome.R
import com.tokopedia.sellerhome.di.component.DaggerSellerHomeComponent
import com.tokopedia.sellerhome.view.adapter.SellerHomeAdapterTypeFactory
import com.tokopedia.sellerhome.view.model.BaseSellerHomeUiModel
import com.tokopedia.sellerhome.view.viewmodel.SellerHomeViewModel
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import javax.inject.Inject

/**
 * Created By @ilhamsuaib on 2020-01-14
 */

class SellerHomeFragment : BaseListFragment<BaseSellerHomeUiModel, SellerHomeAdapterTypeFactory>() {

    companion object {
        @JvmStatic
        fun newInstance() = SellerHomeFragment()
    }

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private val mViewModel by lazy {
        ViewModelProvider(this, viewModelFactory).get(SellerHomeViewModel::class.java)
    }
    private val recyclerView: RecyclerView by lazy { super.getRecyclerView(view) }

    override fun getScreenName(): String = this::class.java.simpleName

    override fun initInjector() {
        DaggerSellerHomeComponent.builder()
                .baseAppComponent((requireContext().applicationContext as BaseMainApplication).baseAppComponent)
                .build()
                .inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_seller_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mViewModel.layoutResponse.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Success -> {
                    renderList(it.data)
                    println("Success : ${Gson().toJsonTree(it.data)}")
                }
                is Fail -> println("Fail")
            }
        })
        mViewModel.getLayout()
    }

    override fun getAdapterTypeFactory(): SellerHomeAdapterTypeFactory {
        return SellerHomeAdapterTypeFactory()
    }

    override fun onItemClicked(t: BaseSellerHomeUiModel?) {

    }

    override fun loadData(page: Int) {

    }
}