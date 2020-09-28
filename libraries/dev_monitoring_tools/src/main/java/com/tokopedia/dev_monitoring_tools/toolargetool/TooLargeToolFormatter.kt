package com.tokopedia.dev_monitoring_tools.toolargetool

import android.app.Activity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.gu.toolargetool.Formatter
import com.gu.toolargetool.TooLargeTool
import com.gu.toolargetool.sizeTreeFromBundle
import com.tokopedia.dev_monitoring_tools.userjourney.UserJourney

class TooLargeToolFormatter(private val minSizeLog: Int, private val userJourneySize: Int = 5): Formatter {
    override fun format(activity: Activity, bundle: Bundle): String {
        val (key, totalSize, subTrees) = sizeTreeFromBundle(bundle)
        var message = ""
        if (totalSize > minSizeLog) {
            message = "size=$totalSize;name=${activity.javaClass.simpleName};activity=${UserJourney.getLastActivity()};journey='${UserJourney.getReadableJourneyActivity(userJourneySize)}';detail='${TooLargeTool.bundleBreakdown(bundle)}'"
        }
        return message
    }

    override fun format(fragmentManager: FragmentManager, fragment: Fragment, bundle: Bundle): String {
        val (key, totalSize, subTrees) = sizeTreeFromBundle(bundle)
        var message = ""
        if (totalSize > minSizeLog) {
            message = "size=$totalSize;name=${fragment.javaClass.simpleName};activity=${UserJourney.getLastActivity()};journey='${UserJourney.getReadableJourneyActivity(userJourneySize)}';detail='${TooLargeTool.bundleBreakdown(bundle)}'"
            val fragmentArguments = fragment.arguments
            if (fragmentArguments != null) {
                message += ";frag_arg=${TooLargeTool.bundleBreakdown(fragmentArguments)}"
            }
        }
        return message
    }
}