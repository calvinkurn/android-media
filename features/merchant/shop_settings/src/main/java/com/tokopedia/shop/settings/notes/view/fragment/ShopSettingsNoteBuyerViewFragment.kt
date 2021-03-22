package com.tokopedia.shop.settings.notes.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.shop.settings.R
import com.tokopedia.shop.settings.common.di.ShopSettingsComponent
import com.tokopedia.shop.settings.notes.view.adapter.ShopNoteBuyerViewAdapter

class ShopSettingsNoteBuyerViewFragment : BaseDaggerFragment() {

    companion object {
        @JvmStatic
        fun createInstance() = ShopSettingsNoteBuyerViewFragment()
    }

    private var rvNote: RecyclerView? = null
    private var adapter: ShopNoteBuyerViewAdapter? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUi()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_shop_settings_note_buyer_view, container, false)
    }

    override fun getScreenName(): String? = null

    override fun initInjector() {
        getComponent(ShopSettingsComponent::class.java).inject(this)
    }

    private fun setupUi() {
        rvNote = view?.findViewById(R.id.rv_note)
        adapter = ShopNoteBuyerViewAdapter()
        rvNote?.adapter = adapter
        rvNote?.layoutManager = LinearLayoutManager(context)
    }

}