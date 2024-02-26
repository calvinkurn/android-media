package com.tokopedia.home_account.account_settings.presentation.adapter.setting

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.home_account.R
import com.tokopedia.home_account.account_settings.presentation.view.GeneralSettingMenuLabel
import com.tokopedia.home_account.account_settings.presentation.view.GeneralSettingMenuLabel.generateSpannableTitle
import com.tokopedia.home_account.account_settings.presentation.viewmodel.SettingItemUIModel
import com.tokopedia.home_account.account_settings.presentation.viewmodel.base.SwitchSettingItemUIModel
import com.tokopedia.unifycomponents.selectioncontrol.SwitchUnify
import com.tokopedia.unifyprinciples.Typography

class GeneralSettingAdapter(
    private var settingItems: List<SettingItemUIModel>?,
    private var listener: OnSettingItemClicked?
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var switchSettingListener: SwitchSettingListener? = null
    fun setSettingItems(settingItems: List<SettingItemUIModel>?) {
        this.settingItems = settingItems
    }

    fun setListener(listener: OnSettingItemClicked?) {
        this.listener = listener
    }

    fun setSwitchSettingListener(switchSettingListener: SwitchSettingListener?) {
        this.switchSettingListener = switchSettingListener
    }

    fun updateSettingItem(settingId: Int) {
        try {
            val position = findSwitchPosition(settingId)
            if (position != POSITION_UNDEFINED) {
                val settingItemUIModel = settingItems!![position]
                if (settingItemUIModel is SwitchSettingItemUIModel) {
                    notifyItemChanged(position)
                }
            }
        } catch (ignored: Throwable) {
        }
    }

    private fun findSwitchPosition(settingId: Int): Int {
        for (i in settingItems!!.indices) {
            if (settingId == settingItems!![i].id) {
                return i
            }
        }
        return POSITION_UNDEFINED
    }

    override fun getItemViewType(position: Int): Int {
        return if (settingItems!![position] is SwitchSettingItemUIModel) {
            TYPE_SWITCH
        } else {
            TYPE_GENERAL
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == TYPE_SWITCH) {
            SwitchSettingViewHolder(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_notif_setting, parent, false)
            )
        } else {
            GeneralSettingViewHolder(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_general_setting, parent, false)
            )
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (getItemViewType(position) == TYPE_SWITCH) {
            (holder as SwitchSettingViewHolder).bind(settingItems!![position] as SwitchSettingItemUIModel)
        } else {
            (holder as GeneralSettingViewHolder).bind(settingItems!![position])
        }
    }

    override fun getItemCount(): Int {
        return if (settingItems != null) {
            settingItems!!.size
        } else {
            0
        }
    }

    internal inner class GeneralSettingViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        private val titleView: Typography
        private val bodyView: Typography
        private val arrowIcon: ImageView

        init {
            titleView = itemView.findViewById(R.id.account_user_item_common_title)
            bodyView = itemView.findViewById(R.id.account_user_item_common_body)
            arrowIcon = itemView.findViewById(R.id.account_user_item_icon_arrow)
            itemView.setOnClickListener { view: View? ->
                if (listener != null) {
                    if (adapterPosition >= 0 && adapterPosition < settingItems!!.size) {
                        listener!!.onItemClicked(settingItems!![adapterPosition].id)
                    }
                }
            }
        }

        fun bind(item: SettingItemUIModel) {
            val title = generateSpannableTitle(
                itemView.context, item, GeneralSettingMenuLabel.LABEL_NEW
            )
            titleView.text = title
            if (item.subtitle != null && item.subtitle!!.isNotEmpty()) {
                bodyView.visibility = View.VISIBLE
                bodyView.text = item.subtitle
            } else bodyView.visibility = View.GONE
            if (item.isHideArrow) {
                arrowIcon.visibility = View.GONE
            } else {
                arrowIcon.visibility = View.VISIBLE
            }
        }
    }

    internal inner class SwitchSettingViewHolder(private val view: View) : RecyclerView.ViewHolder(
        view
    ) {
        private val titleTextView: Typography = itemView.findViewById(R.id.account_user_item_common_title)
        private val summaryTextView: Typography = itemView.findViewById(R.id.account_user_item_common_body)
        private val aSwitch: SwitchUnify = itemView.findViewById(R.id.account_user_item_common_switch)

        init {
            itemView.setOnClickListener { view: View? -> aSwitch.toggle() }
        }

        fun bind(item: SwitchSettingItemUIModel) {
            if (item.labelType().isNotEmpty()) {
                val title = generateSpannableTitle(
                    itemView.context, item, item.labelType()
                )
                titleTextView.text = title
            } else {
                titleTextView.text = item.title
            }
            summaryTextView.text = item.subtitle
            val switchState = switchSettingListener!!.isSwitchSelected(item.id)
            aSwitch.isChecked = switchSettingListener != null && switchState
            if (item.isUseOnClick) {
                view.setOnClickListener { switchSettingListener!!.onClicked(item.id, switchState) }
            }
            aSwitch.setOnCheckedChangeListener { compoundButton: CompoundButton?, isChecked: Boolean ->
                if (switchSettingListener != null) {
                    switchSettingListener!!.onChangeChecked(
                        settingItems!![adapterPosition].id,
                        isChecked
                    )
                }
            }
        }
    }

    interface OnSettingItemClicked {
        fun onItemClicked(settingId: Int)
    }

    interface SwitchSettingListener {
        fun isSwitchSelected(settingId: Int): Boolean
        fun onChangeChecked(settingId: Int, value: Boolean)
        fun onClicked(settingId: Int, currentValue: Boolean)
    }

    companion object {
        private const val TYPE_GENERAL = 0
        private const val TYPE_SWITCH = 1
        private const val POSITION_UNDEFINED = -1
    }
}
