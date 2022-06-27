package com.tokopedia.topchat.common.util

import android.widget.TextView
import rx.subjects.BehaviorSubject
import android.text.TextWatcher
import android.text.Editable
import android.view.View
import rx.subjects.PublishSubject
import android.widget.AbsListView
import android.widget.AdapterView.OnItemClickListener
import android.widget.AutoCompleteTextView
import rx.Observable

/**
 * Created by stevenfredian on 7/13/17.
 */
object Events {
    /*
     * Creates a subject that emits events for the current text and each text change event
     */
    fun text(view: TextView?): Observable<String> {
        val currentText = view?.text.toString()
        val subject = BehaviorSubject.create(currentText)
        view?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i2: Int, i3: Int) {}
            override fun onTextChanged(charSequence: CharSequence, i: Int, i2: Int, i3: Int) {}
            override fun afterTextChanged(editable: Editable) {
                subject.onNext(editable.toString())
            }
        })
        return subject
    }

    /*
     * Creates a subject that emits events for each click on view
     */
    fun click(view: View): Observable<Any> {
        val subject = PublishSubject.create<Any>()
        view.setOnClickListener { subject.onNext(Any()) }
        return subject
    }

    /*
     * Creates a subject that emits events for item clicks of list views
     */
    fun itemClick(view: AbsListView): Observable<Int> {
        val subject = PublishSubject.create<Int>()
        view.onItemClickListener =
            OnItemClickListener { adapterView, view, position, l -> subject.onNext(position) }
        return subject
    }

    fun select(view: AutoCompleteTextView): PublishSubject<Int> {
        val subject = PublishSubject.create<Int>()
        view.onItemClickListener =
            OnItemClickListener { adapterView, view, i, l -> subject.onNext(i + 1) }
        return subject
    }
}