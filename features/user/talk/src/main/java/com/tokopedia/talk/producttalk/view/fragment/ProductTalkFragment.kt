package com.tokopedia.talk.producttalk.view.fragment

import android.content.Context
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.talk.ProductTalkTypeFactoryImpl
import com.tokopedia.talk.R
import com.tokopedia.talk.common.di.TalkComponent
import com.tokopedia.talk.producttalk.di.DaggerProductTalkComponent
import com.tokopedia.talk.producttalk.presenter.ProductTalkPresenter
import com.tokopedia.talk.producttalk.view.adapter.ProductTalkAdapter
import com.tokopedia.talk.producttalk.view.listener.ProductTalkContract
import com.tokopedia.talk.producttalk.view.viewmodel.ProductTalkTitleViewModel
import com.tokopedia.talk.producttalk.view.viewmodel.ProductTalkViewModel
import kotlinx.android.synthetic.main.product_talk.*
import javax.inject.Inject

/**
 * @author by Steven
 */

class ProductTalkFragment : BaseDaggerFragment(), ProductTalkContract.View {

    override fun getContext(): Context? {
        return activity
    }

    override fun getScreenName(): String {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    @Inject
    lateinit var presenter: ProductTalkPresenter
    lateinit var adapter: ProductTalkAdapter
    lateinit var linearLayoutManager: LinearLayoutManager

    var productId: String = ""
    var productName: String = ""
    var productPrice: String = ""
    var productImage: String = ""

    override fun initInjector() {
        val productTalkComponent = DaggerProductTalkComponent.builder()
                .talkComponent(getComponent(TalkComponent::class.java))
                .build()
        productTalkComponent.inject(this)
        presenter.attachView(this)
    }

    companion object {

        fun newInstance(extras: Bundle): ProductTalkFragment {
            val fragment = ProductTalkFragment()
            fragment.productId = extras.getString("product_id")
            fragment.productPrice = extras.getString("product_price")
            fragment.productName = extras.getString("prod_name")
            fragment.productImage = extras.getString("product_image")
            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.product_talk, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpView()
        getProductTalk()
    }

    private fun getProductTalk() {
        presenter.getProductTalk(productId)
    }

    override fun show(viewModel: ProductTalkViewModel) {

        adapter.setList(viewModel.listThread, ProductTalkTitleViewModel(productImage, productName, productPrice))
    }

    private fun setUpView() {
        val adapterTypeFactory = ProductTalkTypeFactoryImpl()
        val listProductTalk = ArrayList<Visitable<*>>()
        adapter = ProductTalkAdapter(adapterTypeFactory, listProductTalk)
        linearLayoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        list_thread.layoutManager = linearLayoutManager
        list_thread.adapter = adapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        presenter.detachView()
    }
}