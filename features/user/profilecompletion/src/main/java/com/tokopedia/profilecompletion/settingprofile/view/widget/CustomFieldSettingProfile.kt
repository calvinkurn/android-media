package com.tokopedia.profilecompletion.settingprofile.view.widget

import android.annotation.TargetApi
import android.content.Context
import android.os.Build
import android.util.AttributeSet
import android.view.View
import android.view.View.OnClickListener
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.profilecompletion.R
import com.tokopedia.unifycomponents.Label
import com.tokopedia.unifyprinciples.Typography
import kotlinx.android.synthetic.main.custom_field_setting_profile.view.*
import kotlinx.android.synthetic.main.item_empty_field_setting_profile.view.*
import kotlinx.android.synthetic.main.item_filled_field_setting_profile.view.*

/**
 * Created by Ade Fulki on 2019-07-09.
 * ade.hadian@tokopedia.com
 */

class CustomFieldSettingProfile : LinearLayout {

    @JvmOverloads
    constructor(
            context: Context,
            attrs: AttributeSet? = null,
            defStyleAttr: Int = 0)
            : super(context, attrs, defStyleAttr)

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    constructor(
            context: Context,
            attrs: AttributeSet?,
            defStyleAttr: Int,
            defStyleRes: Int)
            : super(context, attrs, defStyleAttr, defStyleRes)

    companion object {
        const val EMPTY_FIELD: Int = 1
        const val FILLED_FIELD: Int = 2
    }

    private val view: View by lazy { View.inflate(context, R.layout.custom_field_setting_profile, this) }
    private val itemEmptyField: View by lazy { view.itemEmptyField }
    private val itemFilledField: View by lazy { view.itemFilledField }
    private val subtitleEmpty: Typography by lazy { view.subtitleEmpty }
    private val subtitleFilled: Typography by lazy { view.subtitleFilled }
    private val valueEmpty: EditText by lazy { view.valueEmpty }
    private val valueFilled: Typography by lazy { view.valueFilled }
    private val verified: Label by lazy { view.verified }
    private val btnEdit: Typography by lazy { view.btnEdit }
    private val btnAdd: IconUnify by lazy { view.btnAdd }
    private val message: Typography by lazy { view.message }

    private var fieldType: Int = 1

    init {
        view
        orientation = VERTICAL
    }

    private fun isEmptyField(): Boolean = fieldType == EMPTY_FIELD

    private fun isFilledField(): Boolean = fieldType == FILLED_FIELD

    private fun setFieldType(fieldType: Int) {
        this.fieldType = fieldType
    }

    fun showEmpty(subtitle: String, hint: String, showButton: Boolean) {

        setFieldType(EMPTY_FIELD)
        setSubtitle(subtitle)
        setHint(hint)
        if (showButton) showButton() else hideButton()
        build()
    }

    fun showEmpty(subtitle: String, hint: String, message: String, showButton: Boolean) {

        setFieldType(EMPTY_FIELD)
        setSubtitle(subtitle)
        setHint(hint)
        setMessage(message)
        if (showButton) showButton() else hideButton()
        build()
    }

    fun showEmpty(
            subtitle: String, hint: String, showButton: Boolean,
            fieldClickListener: OnClickListener) {

        setFieldType(EMPTY_FIELD)
        setSubtitle(subtitle)
        setHint(hint)
        if (showButton) showButton() else hideButton()
        setFieldClickListener(fieldClickListener)
        build()
    }

    fun showEmpty(
            subtitle: String, hint: String, message: String, showButton: Boolean,
            fieldClickListener: OnClickListener) {

        setFieldType(EMPTY_FIELD)
        setSubtitle(subtitle)
        setHint(hint)
        setMessage(message)
        if (showButton) showButton() else hideButton()
        setFieldClickListener(fieldClickListener)
        build()
    }

    fun showFilled(
            subtitle: String, value: String, showVerified: Boolean, showButton: Boolean) {
        setFieldType(FILLED_FIELD)
        setSubtitle(subtitle)
        setValue(value)
        if (showVerified) showVerified()
        if (showButton) showButton() else hideButton()
        build()
    }

    fun showFilled(
            subtitle: String, value: String, showVerified: Boolean, showButton: Boolean,
            fieldClickListener: OnClickListener = OnClickListener {

            }) {

        setFieldType(FILLED_FIELD)
        setSubtitle(subtitle)
        setValue(value)
        if (showVerified) showVerified()
        if (showButton) showButton() else hideButton()
        setFieldClickListener(fieldClickListener)
        build()
    }

    override fun setEnabled(enabled: Boolean) {
        if(!enabled) {
            if (isEmptyField()) {
                btnAdd.setOnClickListener(null)
                valueEmpty.setOnClickListener(null)
            } else {
                btnEdit.setOnClickListener(null)
                valueFilled.setOnClickListener(null)
            }
        }

        if(enabled){
            showButton()
        }else{
            hideButton()
        }
    }

    private fun build() {
        if (isEmptyField()) {
            itemEmptyField.visibility = View.VISIBLE
            itemFilledField.visibility = View.GONE
        } else {
            itemEmptyField.visibility = View.GONE
            itemFilledField.visibility = View.VISIBLE
        }
    }

    private fun setSubtitle(text: String) {
        if (isEmptyField()) {
            subtitleEmpty.text = text
            subtitleEmpty.visibility = View.VISIBLE
        } else {
            subtitleFilled.text = text
            subtitleFilled.visibility = View.VISIBLE
        }
    }

    private fun setValue(text: String) {
        if (isFilledField()) {
            valueFilled.text = text
            valueFilled.visibility = View.VISIBLE
        }
    }

    private fun setHint(text: String) {
        if (isEmptyField()) {
            valueEmpty.hint = text
            valueEmpty.visibility = View.VISIBLE
        }
    }

    private fun setMessage(text: String) {
        if (isEmptyField()) {
            message.text = text
            message.visibility = View.VISIBLE
        }
    }

    private fun showVerified() {
        if (isFilledField()) verified.visibility = View.VISIBLE
    }

    private fun showButton() {
        if (isEmptyField())
            btnAdd.visibility = View.VISIBLE
        else btnEdit.visibility = View.VISIBLE
    }

    private fun hideButton() {
        btnAdd.visibility = View.GONE
        btnEdit.visibility = View.GONE
    }

    private fun setFieldClickListener(clickListener: OnClickListener) {
        if (isEmptyField()) {
            btnAdd.setOnClickListener(clickListener)
            valueEmpty.setOnClickListener(clickListener)
        } else {
            btnEdit.setOnClickListener(clickListener)
            valueFilled.setOnClickListener(clickListener)
        }
    }
}