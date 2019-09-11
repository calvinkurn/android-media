package com.tokopedia.design.text.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

import android.app.Activity;
import android.app.Instrumentation;
import android.os.Build;
import androidx.test.InstrumentationRegistry;
import androidx.test.filters.SmallTest;
import androidx.test.rule.ActivityTestRule;
import androidx.test.runner.AndroidJUnit4;
import com.tokopedia.design.test.R;
import com.tokopedia.design.text.TkpdHintTextInputLayout;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@SmallTest
@RunWith(AndroidJUnit4.class)

public class TkpdHintTextInputLayoutTest {

    @Rule
    public ActivityTestRule<TkpdHintTextInputLayoutTestActivity> mActivityRule =
            new ActivityTestRule<>(TkpdHintTextInputLayoutTestActivity.class);

    private void setContentView(final int layoutId) throws Throwable {
        final Activity activity = mActivityRule.getActivity();
        mActivityRule.runOnUiThread(() -> activity.setContentView(layoutId));
    }

    @Test
    public void inflation() throws Throwable {
        setContentView(R.layout.tkpd_hint_text_input_layout_test);
        getInstrumentation().waitForIdleSync();

        TkpdHintTextInputLayout view;
        view = getActivity().findViewById(R.id.text_input_layout);
        assertFalse(view.isCounterEnabled());
        assertFalse(view.isSuccessShown());
        assertEquals(-1, view.getCounterMaxLength());
        assertEquals(view.getEditText().getId(), R.id.edit_text);
        assertNull(view.getError());

    }

    private Activity getActivity() {
        return mActivityRule.getActivity();
    }

    private Instrumentation getInstrumentation() {
        return InstrumentationRegistry.getInstrumentation();
    }
}
