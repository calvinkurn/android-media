/*
 * Copyright 2019 Tokopedia
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Created by Hendy
 * Edited by Meyta
 */

package com.tokopedia.coachmark

import android.app.Activity
import android.app.Dialog
import android.app.DialogFragment
import android.os.Bundle
import android.os.Handler
import android.support.v4.widget.NestedScrollView
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.ScrollView
import com.tokopedia.coachmark.util.ViewHelper
import java.util.*


class CoachMark : DialogFragment() {

    private var tutorsList: ArrayList<CoachMarkItem>? = null
    private var currentTutorIndex = -1
    private var builder: CoachMarkBuilder? = null
    private var coachMarkTag: String? = null

    internal var hasViewGroupHandled = false

    private var listener: OnShowCaseStepListener? = null

    private var retryCounter = 0

    interface OnShowCaseStepListener {
        /**
         * @param previousStep
         * @param nextStep
         * @param coachMarkItem
         * @return true if already fully handled show case step inthis function
         */
        fun onShowCaseGoTo(previousStep: Int, nextStep: Int, coachMarkItem: CoachMarkItem): Boolean
    }

    fun setShowCaseStepListener(listener: OnShowCaseStepListener) {
        this.listener = listener
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getArgs(arguments)
        retainInstance = true
    }

    private fun getArgs(args: Bundle?) {
        if (args != null) {
            builder = args.get(ARG_BUILDER) as CoachMarkBuilder
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = object : Dialog(activity, R.style.CoachMark) {
            override fun onBackPressed() {
                previous()
            }
        }
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        return dialog
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = CoachMarkLayout(activity, builder)
        initViews(view)
        return view
    }

    private fun initViews(view: CoachMarkLayout) {
        view.setShowCaseListener(object : CoachMarkListener {
            override fun onPrevious() {
                previous()
            }

            override fun onNext() {
                next()
            }

            override fun onComplete() {
                if (!TextUtils.isEmpty(tag)) {
                    CoachMarkPreference.setShown(activity, tag, true)
                }
                this@CoachMark.close()
            }
        })

        isCancelable = true
    }

    operator fun next() {
        if (currentTutorIndex + 1 >= tutorsList!!.size) {
            this.close()
        } else {
            this@CoachMark.show(activity, tag, tutorsList, currentTutorIndex + 1)
        }
    }

    fun previous() {
        if (currentTutorIndex - 1 < 0) {
            currentTutorIndex = 0
        } else {
            this@CoachMark.show(activity, tag, tutorsList, currentTutorIndex - 1)
        }
    }

    override fun onStart() {
        super.onStart()
        val window = dialog.window
        if (window != null) {
            window.setBackgroundDrawableResource(android.R.color.transparent)
            window.setDimAmount(0f)
            window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        }
    }

    fun hasShown(activity: Activity, tag: String): Boolean {
        return CoachMarkPreference.hasShown(activity, tag)
    }

    @JvmOverloads
    fun show(activity: Activity?, tag: String?, tutorList: ArrayList<CoachMarkItem>?, index: Int = 0) {
        var indexToShow = index
        if (activity == null || activity.isFinishing) {
            return
        }
        try {
            this.tutorsList = tutorList
            this.coachMarkTag = tag
            if (indexToShow < 0 || indexToShow >= tutorList!!.size) {
                indexToShow = 0
            }
            val previousIndex = currentTutorIndex
            currentTutorIndex = indexToShow

            hasViewGroupHandled = false
            if (listener != null) {
                hasViewGroupHandled = listener!!.onShowCaseGoTo(previousIndex, currentTutorIndex, tutorList!![currentTutorIndex])
            }

            // has been handled by listener
            if (hasViewGroupHandled) return

            val coachMarkItem = tutorList!![currentTutorIndex]
            val viewGroup = coachMarkItem.scrollView
            if (viewGroup != null) {
                val viewToFocus = coachMarkItem.view
                if (viewToFocus != null) {
                    hideLayout()
                    viewGroup.post {
                        if (viewGroup is ScrollView) {
                            val relativeLocation = IntArray(2)
                            ViewHelper.getRelativePositionRec(viewToFocus, viewGroup, relativeLocation)
                            viewGroup.smoothScrollTo(0, relativeLocation[1])
                            viewGroup.postDelayed({ showLayout(activity, coachMarkItem) }, DELAY_SCROLLING.toLong())
                        } else if (viewGroup is NestedScrollView) {
                            val relativeLocation = IntArray(2)
                            ViewHelper.getRelativePositionRec(viewToFocus, viewGroup, relativeLocation)
                            viewGroup.smoothScrollTo(0, relativeLocation[1])
                            viewGroup.postDelayed({ showLayout(activity, coachMarkItem) }, DELAY_SCROLLING.toLong())
                        }
                    }
                    hasViewGroupHandled = true
                } else {
                    hasViewGroupHandled = false
                }
            }

            if (!hasViewGroupHandled) {
                showLayout(activity, tutorsList!![currentTutorIndex])
            }
        } catch (e: Exception) {
            // to Handle the unknown exception.
            // Since this only for first guide, if any error appears, just don't show the guide
            try {
                this@CoachMark.dismiss()
            } catch (e2: Exception) {
                // no op
            }

        }

    }

    fun showLayout(activity: Activity?, coachMarkItem: CoachMarkItem) {
        if (activity == null || activity.isFinishing) {
            return
        }
        val fm = activity.fragmentManager
        if (!isVisible) {
            try {
                if (!isAdded) {
                    show(fm, TAG)
                } else if (isHidden) {
                    val ft = fm.beginTransaction()
                    ft.show(this@CoachMark)
                    ft.commit()
                }
            } catch (e: IllegalStateException) {
                // called in illegal state. just return.
                return
            }

        }

        val view = coachMarkItem.view
        val title = coachMarkItem.title
        val text = coachMarkItem.description
        val coachMarkContentPosition = coachMarkItem.coachMarkContentPosition
        val tintBackgroundColor = coachMarkItem.tintBackgroundColor
        val location = coachMarkItem.location
        val radius = coachMarkItem.radius

        view?.post {
            layoutShowTutorial(view, title, text, coachMarkContentPosition,
                    tintBackgroundColor, location, radius)
        } ?: layoutShowTutorial(null, title, text, coachMarkContentPosition,
                tintBackgroundColor, location, radius)
    }

    fun hideLayout() {
        val layout = this@CoachMark.view as CoachMarkLayout ?: return
        layout.hideTutorial()
    }

    private fun layoutShowTutorial(view: View?, title: String?, text: String,
                                   coachMarkContentPosition: CoachMarkContentPosition,
                                   tintBackgroundColor: Int, customTarget: IntArray?, radius: Int) {

        try {
            val layout = this@CoachMark.view as CoachMarkLayout
            if (layout == null) {
                if (retryCounter >= MAX_RETRY_LAYOUT) {
                    retryCounter = 0
                    return
                }
                // wait until the layout is ready, and call itself
                Handler().postDelayed({
                    retryCounter++
                    layoutShowTutorial(view, title, text,
                            coachMarkContentPosition, tintBackgroundColor, customTarget, radius)
                }, 1000)
                return
            }
            retryCounter = 0
            layout.showTutorial(view, title, text, currentTutorIndex, tutorsList!!.size,
                    coachMarkContentPosition, tintBackgroundColor, customTarget, radius)
        } catch (t: Throwable) {
            // do nothing
        }

    }

    fun close() {
        try {
            dismiss()
            val layout = this@CoachMark.view as CoachMarkLayout ?: return
            layout.closeTutorial()
        } catch (e: Exception) {
            // no op
        }

    }

    companion object {

        private val ARG_BUILDER = "BUILDER"
        val DELAY_SCROLLING = 350
        val TAG = "CoachMark"
        val MAX_RETRY_LAYOUT = 3

        internal fun newInstance(builder: CoachMarkBuilder): CoachMark {
            val args = Bundle()
            val fragment = CoachMark()
            args.putParcelable(ARG_BUILDER, builder)
            fragment.arguments = args
            return fragment
        }
    }
}
