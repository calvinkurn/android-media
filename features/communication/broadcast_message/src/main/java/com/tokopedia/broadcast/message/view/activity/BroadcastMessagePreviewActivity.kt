package com.tokopedia.broadcast.message.view.activity

import android.content.Context
import android.content.Intent
import android.support.v4.app.Fragment
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.broadcast.message.common.di.component.BroadcastMessageComponent
import com.tokopedia.broadcast.message.common.di.component.DaggerBroadcastMessageComponent
import com.tokopedia.broadcast.message.R
import com.tokopedia.broadcast.message.data.model.BlastMessageMutation
import com.tokopedia.broadcast.message.view.fragment.BroadcastMessagePreviewFragment

class BroadcastMessagePreviewActivity: BaseSimpleActivity(), HasComponent<BroadcastMessageComponent> {

    companion object {
        private const val PARAM_EXTRA_MODEL_MUTATION = "model_mutation"
        fun createIntent(context: Context, mutationModel: BlastMessageMutation) =
                Intent(context, BroadcastMessagePreviewActivity::class.java)
                        .putExtra(PARAM_EXTRA_MODEL_MUTATION, mutationModel)
    }

    override fun getNewFragment(): Fragment {
        val mutationModel: BlastMessageMutation = intent.getParcelableExtra(PARAM_EXTRA_MODEL_MUTATION)
        return BroadcastMessagePreviewFragment.createInstance(mutationModel)
    }

    override fun getComponent() = DaggerBroadcastMessageComponent.builder().baseAppComponent(
            (application as BaseMainApplication).getBaseAppComponent()).build()

    override fun getLayoutRes() = R.layout.activity_bm_preview
}