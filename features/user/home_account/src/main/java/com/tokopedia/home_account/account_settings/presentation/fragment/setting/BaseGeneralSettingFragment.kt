package com.tokopedia.home_account.account_settings.presentation.fragment.setting

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.fragment.TkpdBaseV4Fragment
import com.tokopedia.home_account.R
import com.tokopedia.home_account.account_settings.presentation.adapter.setting.GeneralSettingAdapter
import com.tokopedia.home_account.account_settings.presentation.adapter.setting.GeneralSettingAdapter.OnSettingItemClicked
import com.tokopedia.home_account.account_settings.presentation.viewmodel.SettingItemUIModel
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface

abstract class BaseGeneralSettingFragment : TkpdBaseV4Fragment(), OnSettingItemClicked {
    protected var recyclerView: RecyclerView? = null
    protected var userSession: UserSessionInterface? = null
    protected var adapter: GeneralSettingAdapter? = null
    override fun onAttach(context: Context) {
        super.onAttach(context)
        userSession = UserSession(activity)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_base_setting, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView = view.findViewById(R.id.recyclerview)
        recyclerView?.layoutManager = LinearLayoutManager(
            activity,
            LinearLayoutManager.VERTICAL,
            false
        )
        recyclerView?.setHasFixedSize(true)
        adapter = GeneralSettingAdapter(getSettingItems(), this)
        recyclerView?.adapter = adapter
    }

    protected abstract fun getSettingItems(): List<SettingItemUIModel>?
}
