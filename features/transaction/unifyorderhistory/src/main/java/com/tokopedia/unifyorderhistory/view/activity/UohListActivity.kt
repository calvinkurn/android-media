package com.tokopedia.unifyorderhistory.view.activity

import android.net.Uri
import android.os.Bundle
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.applink.internal.ApplinkConstInternalOrder.FILTER
import com.tokopedia.applink.internal.ApplinkConstInternalOrder.SOURCE_FILTER
import com.tokopedia.unifyorderhistory.view.fragment.UohListFragment
import com.tokopedia.kotlin.extensions.view.toEmptyStringIfNull
import com.tokopedia.unifyorderhistory.di.UohListComponent
import com.tokopedia.unifyorderhistory.di.UohListModule

/**
 * Created by fwidjaja on 29/06/20.
 */

// Uoh = Unified Order History
class UohListActivity: BaseSimpleActivity(), HasComponent<UohListComponent> {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        component.inject(this)
    }

    override fun getNewFragment(): UohListFragment {
        val bundle = Bundle()
        scanPathQuery(intent.data)
        if (intent != null && intent.extras != null) {
            bundle.putAll(intent.extras)
        }
        return UohListFragment.newInstance(bundle)
    }

    private fun scanPathQuery(data: Uri?) {
        data?.let {
            val filterStatus = it.getQueryParameter(FILTER).toEmptyStringIfNull()
            intent.putExtra(SOURCE_FILTER, filterStatus)
        }
    }

    override fun getComponent(): UohListComponent {
        return DaggerUohListComponent.builder()
            .baseAppComponent((application as BaseMainApplication).baseAppComponent)
            .uohListModule(UohListModule(this))
            .build()
    }
}