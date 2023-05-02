package com.tokopedia.developer_options.presentation.viewholder

import android.content.Context
import android.text.TextUtils
import android.view.View
import android.widget.Toast
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.analyticsdebugger.serverlogger.presentation.activity.ServerLoggerActivity
import com.tokopedia.developer_options.R
import com.tokopedia.developer_options.presentation.model.LoggingToServerUiModel
import com.tokopedia.logger.ServerLogger.log
import com.tokopedia.logger.utils.Priority
import com.tokopedia.unifycomponents.TextFieldUnify
import com.tokopedia.unifycomponents.UnifyButton

class LoggingToServerViewHolder(
    itemView: View
) : AbstractViewHolder<LoggingToServerUiModel>(itemView) {
    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_logging_to_server
    }

    override fun bind(element: LoggingToServerUiModel) {
        val btn = itemView.findViewById<UnifyButton>(R.id.logging_to_server_btn)
        val tf = itemView.findViewById<TextFieldUnify>(R.id.logging_to_server_tf)
        val viewServerLoggerBtn = itemView.findViewById<UnifyButton>(R.id.view_server_logger_btn)
        btn.setOnClickListener {
            val timberMessage: String = tf.textFieldInput.text.toString()
            itemView.context.apply {
                if (timberMessage.isBlank()) {
                    Toast.makeText(this, "Timber message should not empty", Toast.LENGTH_SHORT)
                        .show()
                } else {
                    manageTimberMessage(this, timberMessage)
                }
            }
        }
        viewServerLoggerBtn.setOnClickListener {
            itemView.context.run {
                startActivity(ServerLoggerActivity.newInstance(this))
            }
        }
    }

    private fun manageTimberMessage(context: Context, timberMessage: String) {
        // "Pno##TAG##message (message: abc=123##edf=456) (required Server Logging ON)"
        val priorityIndex = 0
        val tagIndex = 1
        var priority = ""
        var tag = ""
        val delimiterMessage = "##"
        val regexEqualSign = "=([*]*)"
        val messageMap: MutableMap<String, String> = HashMap()
        val splitMessage = timberMessage.split(delimiterMessage).toTypedArray()
        for (i in splitMessage.indices) {
            if (i == priorityIndex) {
                priority = splitMessage[priorityIndex]
            } else if (i == tagIndex) {
                tag = splitMessage[tagIndex]
            } else {
                val message = splitMessage[i]
                if (!TextUtils.isEmpty(message)) {
                    val keyValue = message.split(regexEqualSign.toRegex()).toTypedArray()
                    if (getOrNull(keyValue, 0) != null && getOrNull(keyValue, 1) != null) {
                        messageMap[keyValue[0]] = keyValue[1]
                    } else {
                        Toast.makeText(context, "Invalid timber message format", Toast.LENGTH_LONG)
                            .show()
                        return
                    }
                }
            }
        }
        var priorityLogger: Priority? = null
        if (priority == "P1") {
            priorityLogger = Priority.P1
        } else if (priority == "P2") {
            priorityLogger = Priority.P2
        }
        if (priorityLogger != null) {
            log(priorityLogger, tag, messageMap)
            Toast.makeText(context, "$timberMessage has been sent", Toast.LENGTH_LONG).show()
        }
    }

    private fun getOrNull(list: Array<String>, index: Int): Any? {
        return if (index >= 0 && index <= list.size - 1) {
            list[index]
        } else {
            null
        }
    }
}
