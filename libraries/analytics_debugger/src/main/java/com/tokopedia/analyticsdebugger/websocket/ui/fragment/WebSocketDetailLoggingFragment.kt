package com.tokopedia.analyticsdebugger.websocket.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.tokopedia.analyticsdebugger.R
import com.tokopedia.analyticsdebugger.databinding.FragmentWebsocketDetailLoggingBinding
import com.tokopedia.analyticsdebugger.websocket.ui.uimodel.WebSocketLogUiModel
import com.tokopedia.analyticsdebugger.websocket.ui.uimodel.info.PlayUiModel
import com.tokopedia.analyticsdebugger.websocket.ui.uimodel.info.TopchatUiModel
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.utils.view.binding.viewBinding

/**
 * Created By : Jonathan Darwin on December 20, 2021
 */
class WebSocketDetailLoggingFragment: Fragment() {

    private val binding: FragmentWebsocketDetailLoggingBinding? by viewBinding()

    private val model: WebSocketLogUiModel? by lazy {
        WebSocketDetailLoggingFragmentArgs
            .fromBundle(arguments!!).model
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_websocket_detail_logging, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setData()
    }

    private fun setData() {
        onShowLogEventLabel()
        onShowLogMessage()

        when {
            model?.play != null -> onPlayDetailInfoLoaded(model?.play)
            model?.topchat != null -> onTopchatDetailInfoLoaded(model?.topchat)
        }
    }

    private fun onShowLogEventLabel() {
        binding?.tvWebsocketDetailLogDateTime?.text = model?.dateTime
        binding?.tvWebsocketDetailLogTitle?.text = model?.event
    }

    private fun onShowLogMessage() {
        val shouldShow = model?.message?.isNotEmpty() == true

        binding?.tvWebsocketPayloadLabel?.showWithCondition(shouldShow)
        binding?.tvWebsocketDetailLogMessage?.showWithCondition(shouldShow)

        binding?.tvWebsocketDetailLogMessage?.text = model?.message
    }

    private fun onPlayDetailInfoLoaded(data: PlayUiModel?) {
        setLogDetailFirst(R.string.websocket_log_channel_id_label, data?.channelId.toString())
        setLogDetailSecond(R.string.warehouse_id, data?.warehouseId.toString())
        setLogDetailThird(R.string.websocket_log_gc_token_label, data?.gcToken.toString())
    }

    private fun onTopchatDetailInfoLoaded(data: TopchatUiModel?) {
        setLogDetailFirst(R.string.websocket_log_code_id_label, data?.code.toString())
        setLogDetailSecond(R.string.websocket_log_message_id_label, data?.messageId.toString())
        setHeaderRequest(data?.header.toString())
    }

    private fun setLogDetailFirst(label: Int, data: String) {
        binding?.tvWebsocketDetailLog1Label?.text = getString(label)
        binding?.tvWebsocketDetailLog1?.text = data
        binding?.tvWebsocketDetailLog1Label?.showWithCondition(data.isNotEmpty())
        binding?.tvWebsocketDetailLog1?.showWithCondition(data.isNotEmpty())
    }

    private fun setLogDetailSecond(label: Int, data: String) {
        binding?.tvWebsocketDetailLog2Label?.text = getString(label)
        binding?.tvWebsocketDetailLog2?.text = data
        binding?.tvWebsocketDetailLog2Label?.showWithCondition(data.isNotEmpty())
        binding?.tvWebsocketDetailLog2?.showWithCondition(data.isNotEmpty())
    }

    private fun setLogDetailThird(label: Int, data: String) {
        binding?.tvWebsocketDetailLog3Label?.text = getString(label)
        binding?.tvWebsocketDetailLog3?.text = data
        binding?.tvWebsocketDetailLog3Label?.showWithCondition(data.isNotEmpty())
        binding?.tvWebsocketDetailLog3?.showWithCondition(data.isNotEmpty())
    }

    private fun setHeaderRequest(header: String) {
        binding?.tvWebsocketDetailLogHeader?.text = header
        binding?.tvWebsocketDetailLogHeader?.showWithCondition(header.isNotEmpty())
        binding?.tvWebsocketHeaderLabel?.showWithCondition(header.isNotEmpty())
    }
}
