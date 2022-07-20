package com.tokopedia.developer_options.presentation.viewholder

import android.view.View
import android.widget.*
import android.widget.AdapterView.OnItemSelectedListener
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.developer_options.R
import com.tokopedia.developer_options.presentation.model.UrlEnvironmentUiModel
import com.tokopedia.url.Env
import com.tokopedia.url.TokopediaUrl.Companion.deleteInstance
import com.tokopedia.url.TokopediaUrl.Companion.getInstance
import com.tokopedia.url.TokopediaUrl.Companion.init
import com.tokopedia.url.TokopediaUrl.Companion.setEnvironment

class UrlEnvironmentViewHolder(
    itemView: View,
    private val listener: UrlEnvironmentListener
): AbstractViewHolder<UrlEnvironmentUiModel>(itemView)
{
    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_url_environment
    }

    private var isUserEditEnvironment = true
    private var spinner: Spinner? = null

    override fun bind(element: UrlEnvironmentUiModel?) {
        spinner = itemView.findViewById(R.id.env_chooser_spinner)
        setSpinnerAdapter()
        setSpinnerValue()
        setSpinnerSelected()
    }

    private fun setSpinnerAdapter() {
        val envSpinnerAdapter: ArrayAdapter<Env> = ArrayAdapter<Env>(itemView.context, R.layout.customized_spinner_item, Env.values())
        envSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner?.adapter = envSpinnerAdapter
    }

    private fun setSpinnerValue() {
        val currentEnv = getInstance().TYPE
        for (i in Env.values().indices) {
            if (currentEnv == Env.values()[i]) {
                isUserEditEnvironment = false
                spinner?.setSelection(i)
                break
            }
        }
    }

    private fun setSpinnerSelected() {
        spinner?.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View,
                position: Int,
                id: Long
            ) {
                if (isUserEditEnvironment) {
                    setEnvironment(itemView.context, Env.values()[position])
                    deleteInstance()
                    init(itemView.context)
                    listener.onLogOutUserSession()
                    Toast.makeText(itemView.context, "Please Restart the App", Toast.LENGTH_SHORT).show()
                }
                isUserEditEnvironment = true
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }

    interface UrlEnvironmentListener {
        fun onLogOutUserSession()
    }
}