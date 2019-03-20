package com.tokopedia.broadcast.message.view.fragment

import android.animation.ObjectAnimator
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ScrollView
import android.widget.TextView
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.abstraction.common.utils.network.ErrorHandler
import com.tokopedia.broadcast.message.R
import com.tokopedia.broadcast.message.common.BroadcastMessageRouter
import com.tokopedia.broadcast.message.common.constant.BroadcastMessageConstant
import com.tokopedia.broadcast.message.common.di.component.BroadcastMessageComponent
import com.tokopedia.broadcast.message.common.di.component.DaggerBroadcastMessagePreviewComponent
import com.tokopedia.broadcast.message.data.model.BlastMessageMutation
import com.tokopedia.broadcast.message.data.model.BlastMessageResponse
import com.tokopedia.broadcast.message.view.listener.BroadcastMessagePreviewView
import com.tokopedia.broadcast.message.view.presenter.BroadcastMessagePreviewPresenter
import com.tokopedia.broadcast.message.view.widget.PreviewProductWidget
import com.tokopedia.design.base.BaseToaster
import com.tokopedia.design.component.ToasterError
import com.tokopedia.graphql.data.GraphqlClient
import com.tokopedia.track.TrackApp
import kotlinx.android.synthetic.main.fragment_broadcast_message_preview.*
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class BroadcastMessagePreviewFragment: BaseDaggerFragment(), BroadcastMessagePreviewView {
    @Inject
    lateinit var presenter: BroadcastMessagePreviewPresenter
    private val texViewMenu: TextView? by lazy { activity?.findViewById(R.id.texViewMenu) as? TextView }
    private val mutationModel by lazy {
        arguments?.run {
            getParcelable(PARAM_EXTRA_MODEL_MUTATION) as? BlastMessageMutation
        }
    }
    private val router: BroadcastMessageRouter? by lazy {
        activity?.application as? BroadcastMessageRouter
    }

    companion object {
        private const val PARAM_EXTRA_MODEL_MUTATION = "model_mutation"

        fun createInstance(mutationModel: BlastMessageMutation) = BroadcastMessagePreviewFragment().apply {
            arguments = Bundle().apply { putParcelable(PARAM_EXTRA_MODEL_MUTATION, mutationModel) }
        }
    }

    override fun getScreenName(): String? = null

    override fun initInjector() {
        DaggerBroadcastMessagePreviewComponent.builder()
                .broadcastMessageComponent(getComponent(BroadcastMessageComponent::class.java))
                .build().inject(this)
        presenter.attachView(this)
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        context?.let { GraphqlClient.init(it) }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_broadcast_message_preview, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        texViewMenu?.run {
            text = getString(R.string.send_message)
            setOnClickListener {
                submitMessage()
            }
        }
        mutationModel?.let { setupPreview(it) }
        scroll_view.post { ObjectAnimator.ofInt(scroll_view, "scrollY",  container_screen.height)
                .setDuration(3000).start(); }
    }

    private fun setupPreview(model: BlastMessageMutation) {
        ImageHandler.LoadImage(image, model.imagePath)
        if (model.hasProducts && context != null){
            model.productsPayload.forEach {
                val tmpViewProduct = PreviewProductWidget(context!!)
                tmpViewProduct.setupBubble()
                tmpViewProduct.bindProduct(it)
                container_product_attach.addView(tmpViewProduct)
            }
        }
        message.text = model.message
        time_message.text = getString(R.string.time_template, getTimeNow())
    }

    private fun getTimeNow() = SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date())

    private fun submitMessage() {
        TrackApp.getInstance()?.gtm?.sendGeneralEvent(BroadcastMessageConstant.VALUE_GTM_EVENT_NAME_INBOX,
                    BroadcastMessageConstant.VALUE_GTM_EVENT_CATEGORY,
                    BroadcastMessageConstant.VALUE_GTM_EVENT_ACTION_SUBMIT, "")
        mutationModel?.let {presenter.sendBlastMessage(it)}
    }

    override fun onDestroyView() {
        super.onDestroyView()
        presenter.detachView()
    }

    override fun showLoading() {
        loading.visibility = View.VISIBLE
    }

    override fun hideLoading() {
        loading.visibility = View.GONE
    }

    override fun onErrorSubmitBlastMessage(t: Throwable?) {
        if (view == null || context == null) return
        ToasterError.make(view, ErrorHandler.getErrorMessage(context!!, t), BaseToaster.LENGTH_LONG)
                .setAction(R.string.retry){
                    presenter.sendBlastMessage(mutationModel!!)
                }.show()
    }

    override fun onSuccessSubmitBlastMessage(result: BlastMessageResponse) {
        val data = Intent().putExtra(BroadcastMessageConstant.PARAM_NEED_REFRESH, result.success)
        activity?.run {
            setResult(Activity.RESULT_OK, data)
            finish()
        }
    }
}