package com.tokopedia.design.text;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v7.widget.TintTypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;

import com.tokopedia.design.R;
import com.tokopedia.util.TestLayoutInflater;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.robolectric.RobolectricTestRunner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyBoolean;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(PowerMockRunner.class)
@PrepareForTest(LayoutInflater.class)
public class TkpdHintTextInputLayoutTest {

    @Before
    public void setup() {
    }

    @Test
    public void testCase1(){
        Context context = mock(Context.class);
        TypedArray typedArray = mock(TypedArray.class);
        TestLayoutInflater layoutInflater = new TestLayoutInflater(context);
        initMockAttributes(context, typedArray, layoutInflater);

        TkpdHintTextInputLayout tkpdHintTextInputLayout
                = new TkpdHintTextInputLayout(context, mock(AttributeSet.class));

        assertThat(layoutInflater.getResId()).isEqualTo(R.layout.hint_text_input_layout);
        assertThat(layoutInflater.getRoot()).isEqualTo(tkpdHintTextInputLayout);
    }

    private void initMockAttributes(Context context, TypedArray typedArray, LayoutInflater layoutInflater) {
        when(context.obtainStyledAttributes(
                any(AttributeSet.class),
                eq(R.styleable.TkpdHintTextInputLayout),
                anyInt(),
                eq(android.support.design.R.style.Widget_Design_TextInputLayout))
        ).thenReturn(typedArray);
        when(typedArray.getBoolean(eq(R.styleable.TkpdHintTextInputLayout_hintEnabled), anyBoolean()))
                .thenReturn(true);
        when(context.getSystemService(Context.LAYOUT_INFLATER_SERVICE))
                .thenReturn(layoutInflater);
    }
}
