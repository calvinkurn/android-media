package com.tokopedia.talk

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import kotlinx.android.synthetic.main.product_talk.*

/**
 * @author by nisie on 6/7/18.
 */

class ProductTalkFragment : ProductTalkContract.View, BaseDaggerFragment() {

    override fun getScreenName(): String {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    lateinit var presenter: ProductTalkPresenter
    lateinit var adapter: ProductTalkAdapter
    lateinit var linearLayoutManager: LinearLayoutManager

    override fun initInjector() {
        presenter = ProductTalkDependencyInjector.Companion.inject(activity!!.applicationContext)
        presenter.attachView(this)
    }

    companion object {

        fun newInstance(): ProductTalkFragment {
            return ProductTalkFragment()
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
        presenter.getProductTalk()
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