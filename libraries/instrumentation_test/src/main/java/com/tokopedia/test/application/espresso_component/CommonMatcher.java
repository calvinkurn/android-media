package com.tokopedia.test.application.espresso_component;

import android.view.View;

import org.hamcrest.Matcher;
import org.hamcrest.Matchers;

import static androidx.test.espresso.matcher.ViewMatchers.withTagValue;

public class CommonMatcher {
    public static Matcher<View> withTagStringValue(String tagStringValue) {
        return withTagValue(Matchers.is(tagStringValue));
    }
}
