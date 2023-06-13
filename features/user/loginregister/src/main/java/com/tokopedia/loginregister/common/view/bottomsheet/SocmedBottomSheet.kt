package com.tokopedia.loginregister.common.view.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.loginregister.R
import com.tokopedia.loginregister.databinding.LayoutSocmedBottomsheetBinding
import com.tokopedia.loginregister.discover.pojo.ProviderData
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.utils.lifecycle.autoClearedNullable

/**
 * Created by Yoris Prayogo on 15/12/20.
 * Copyright (c) 2020 PT. Tokopedia All rights reserved.
 */

interface SocmedBottomSheetListener {
    fun onItemClick(provider: ProviderData)
}

class SocmedBottomSheet(
): BottomSheetUnify() {

    var listener: SocmedBottomSheetListener? = null

    private var viewBinding by autoClearedNullable<LayoutSocmedBottomsheetBinding>()
    private var socmedAdapter: SocmedBottomSheetAdapter? = null
    private var providers: MutableList<ProviderData> = mutableListOf()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewBinding = LayoutSocmedBottomsheetBinding.inflate(
            inflater
        ).also {
            setChild(it.root)
        }

        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setTitle(context?.getString(R.string.choose_social_media) ?: "")

        socmedAdapter = SocmedBottomSheetAdapter(providers, listener)
        viewBinding?.socmedList?.run {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            adapter = socmedAdapter
        }
    }

    fun setProviders(providers: List<ProviderData>) {
        this.providers.clear()
        this.providers.addAll(providers)
        socmedAdapter?.notifyItemRangeInserted(0, providers.size)
    }
}
