package com.tokopedia.webview.ext

import android.content.Context
import android.graphics.Color
import androidx.core.content.ContextCompat

private fun generateColorRGBAString(colorInt: Int): String {
    return "rgba(${Color.red(colorInt)},${Color.green(colorInt)},${Color.blue(colorInt)},${Color.alpha(colorInt).toFloat() / 255})"
}

fun constructContentToHtml(context: Context, content: String): String {
    val backgroundColor = generateColorRGBAString(ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_Background))
    val pColor = generateColorRGBAString(ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_NN950_96))
    val htmlText = """
<html>
	<style>
    body {
        background: $backgroundColor;
        font-family: SFProText; 
        font-size: 14px;
        line-height: 18px;
        color: $pColor;
    }
   
    ol li {
        font-family: SFProText;
        font-size: 14px;
        line-height: 18px;
        color: $pColor;
        margin-top:8px;
    }
    ol {
        padding-inline-start: 12px;
    }
    p {
        font-family: SFProText; 
        font-size: 14px;
        line-height: 18px;
        color: $pColor;
    }
    </style>
    <body>
        $content
    </body>
</html>
        """.trimIndent()
    return htmlText
}