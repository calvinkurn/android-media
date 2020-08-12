package com.tokopedia.developer_options.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.appcompat.widget.AppCompatEditText
import androidx.fragment.app.Fragment
import com.tokopedia.developer_options.R

class FeedbackPageFragment: Fragment() {

    private lateinit var bugType : Spinner

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val mainView = inflater.inflate(R.layout.fragment_feedback_page, container, false)
        initView(mainView)
        initListener()
        return mainView
    }

    private fun initView(mainView: View){
        val email: AppCompatEditText =  mainView.findViewById(R.id.email)
        bugType = mainView.findViewById(R.id.spinner_bug_type)
        val issueDesc: AppCompatEditText = mainView.findViewById(R.id.issue_description)
        val affectedPage: AppCompatEditText = mainView.findViewById(R.id.affected_page)

        context?.let { ArrayAdapter.createFromResource(it,
                R.array.bug_type_array,
                android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            bugType.adapter = adapter
        } }
    }

    private fun initListener() {
        bugType.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View, position: Int, id: Long) {
                /*On Item selected*/
                }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                //no-op
            }
        }
    }

}