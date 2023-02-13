package com.tokopedia.topads.utils

import android.text.Spannable
import android.text.SpannableString
import android.text.TextPaint
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.view.View
import com.tokopedia.topads.constants.SpanConstant

object SpannableUtils {

    private const val NOT_FOUND = -1

    private fun getColoredSpannable(spannable:SpannableString,substring:String,color:Int){
        val startIndex = spannable.indexOf(substring)
        val endIndex = startIndex + substring.length
        if(startIndex!= NOT_FOUND){
            spannable.setSpan(
                ForegroundColorSpan(color),
                startIndex,
                endIndex,
                Spannable.SPAN_INCLUSIVE_EXCLUSIVE
            )
        }
    }

    private fun getTypeFaceSpannable(spannable:SpannableString,substring:String,typeface:Int){
        val startIndex = spannable.indexOf(substring)
        val endIndex = startIndex + substring.length
        if(startIndex!= NOT_FOUND){
            spannable.setSpan(
                StyleSpan(typeface),
                startIndex,
                endIndex,
                Spannable.SPAN_INCLUSIVE_EXCLUSIVE
            )
        }
    }

    private fun getClickSpannable(spannable:SpannableString,substring:String,listener:(() -> Unit)?){
        val startIndex = spannable.indexOf(substring)
        val endIndex = startIndex + substring.length
        if(startIndex!= NOT_FOUND){
            spannable.setSpan(
                object : ClickableSpan(){
                    override fun onClick(v: View) {
                        listener?.invoke()
                    }

                    override fun updateDrawState(ds: TextPaint) {
                        ds.isUnderlineText = false
                    }
                },
                startIndex,
                endIndex,
                Spannable.SPAN_INCLUSIVE_EXCLUSIVE
            )
        }
    }

    /*
       To pass spans, Annote the spanType in Span class with @SpanConstant
       Ex: SpannableUtils.applySpannable(
             "",
             "",
             SpannedString("",listOf(
               Span(@SpanConstant SpanConstant.COLOR_SPAN,0)
             ))
        )
    */
    @Suppress("UNCHECKED_CAST")
    fun applySpannable(originalString:String,vararg spannedItems:SpannedString) : CharSequence{
        val result = SpannableString(originalString)
        spannedItems.forEach {
            it.spans.forEach{ it1 ->
                when(it1.spanType){
                    SpanConstant.COLOR_SPAN -> getColoredSpannable(result,it.substring,(it1 as Span<Int>).spanValue)
                    SpanConstant.TYPEFACE_SPAN -> getTypeFaceSpannable(result,it.substring,(it1 as Span<Int>).spanValue)
                    SpanConstant.CLICK_SPAN -> getClickSpannable(result,it.substring,(it1 as Span<(() -> Unit)?>).spanValue)
                }
            }
        }
        return result
    }
}

class SpannedString(
    val substring:String,
    val spans:List<Span<Any>>
)

data class Span<T>(
    @SpanConstant
    val spanType:Int,
    val spanValue:T
)
