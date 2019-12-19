package com.tokopedia.kol.common.util;

import android.os.Build;
import androidx.annotation.NonNull;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.method.MovementMethod;
import android.text.util.Linkify;
import android.widget.TextView;

import com.tokopedia.abstraction.common.utils.view.MethodChecker;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author by milhamj on 07/05/18.
 */

public class UrlUtil {
    private static final Pattern pattern = Pattern.compile("((https?)://)?(www.)?(tokopedia.com|tkp.me)([-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|])?");
    private static final String[] SCHEMES = {"http://", "https://"};

    public static void setTextWithClickableTokopediaUrl(TextView textView, String text) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            textView.setText(MethodChecker.fromHtml(text));
            Linkify.addLinks(textView, pattern, SCHEMES[0], SCHEMES, null, null);
        } else {
            if (!TextUtils.isEmpty(text)) {
                text = text.replace(SCHEMES[1], SCHEMES[0]);
                textView.setText(MethodChecker.fromHtml(text));
                Linkify.addLinks(textView, pattern, SCHEMES[0]);
            }
        }
    }

    public static void setTextWithClickableTokopediaUrl(TextView textView, String text,
                                                        Object spanBehaviour) {
        if (spanBehaviour == null) {
            setTextWithClickableTokopediaUrl(textView, text);
            return;
        }

        textView.setText(MethodChecker.fromHtml(text));
        SpannableString spannableText = new SpannableString(textView.getText());
        Matcher matcher = pattern.matcher(spannableText);
        while (matcher.find()) {
            spannableText.setSpan(spanBehaviour,
                    matcher.start(),
                    matcher.end(),
                    Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        }
        textView.setText(spannableText);
        addLinkMovementMethod(textView);
    }

    private static void addLinkMovementMethod(@NonNull TextView t) {
        MovementMethod m = t.getMovementMethod();

        if (!(m instanceof LinkMovementMethod)) {
            if (t.getLinksClickable()) {
                t.setMovementMethod(LinkMovementMethod.getInstance());
            }
        }
    }
}
