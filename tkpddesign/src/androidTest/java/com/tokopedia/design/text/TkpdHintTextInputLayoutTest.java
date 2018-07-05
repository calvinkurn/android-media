//package com.tokopedia.design.text;
//
//import static org.junit.Assert.assertEquals;
//import static org.junit.Assert.assertNotNull;
//import static org.junit.Assert.assertThat;
//import static org.mockito.Matchers.any;
//import static org.mockito.Matchers.anyBoolean;
//import static org.mockito.Matchers.anyInt;
//import static org.mockito.Matchers.eq;
//import static org.mockito.Mockito.mock;
//import static org.mockito.Mockito.times;
//import static org.mockito.Mockito.verify;
//import static org.mockito.Mockito.when;
//
//import android.content.Context;
//import android.content.res.TypedArray;
//import android.support.test.InstrumentationRegistry;
//import android.support.test.filters.SmallTest;
//import android.support.test.runner.AndroidJUnit4;
//import android.support.v4.view.LayoutInflaterCompat;
//import android.text.Editable;
//import android.text.Spannable;
//import android.text.SpannableString;
//import android.util.AttributeSet;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.widget.EditText;
//
//import com.tokopedia.design.R;
//import com.tokopedia.design.text.watcher.CurrencyTextWatcher;
//import com.tokopedia.util.TestLayoutInflater;
//
//import org.junit.Before;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//
//@SmallTest
//@RunWith(AndroidJUnit4.class)
//public class TkpdHintTextInputLayoutTest {
//    private View rootView;
//
//    @Before
//    public void setup() {
//        rootView = LayoutInflater.from(InstrumentationRegistry.getContext())
//                .inflate(com.tokopedia.design.test.R.layout.tkpd_hint_text_input_layout_test, null);
//    }
//
//    @Test
//    public void testCase1(){
//        assertNotNull(rootView);
//
//        Context context = mock(Context.class);
//        TypedArray typedArray = mock(TypedArray.class);
//        initMockAttributes(context, typedArray);
//
//        TkpdHintTextInputLayout tkpdHintTextInputLayout
//                = new TkpdHintTextInputLayout(context, mock(AttributeSet.class));
//
//        assertNotNull(tkpdHintTextInputLayout.getEditText());
//    }
//
//    private void initMockAttributes(Context context, TypedArray typedArray) {
//        when(context.obtainStyledAttributes(
//                any(AttributeSet.class),
//                eq(R.styleable.TkpdHintTextInputLayout))
//        ).thenReturn(typedArray);
////        when(context.obtainStyledAttributes(
////                any(AttributeSet.class),
////                eq(R.styleable.TkpdHintTextInputLayout),
////                anyInt(),
////                anyInt())//eq(android.support.design.R.style.Widget_Design_TextInputLayout))
////        ).thenReturn(typedArray);
//        when(typedArray.getBoolean(eq(R.styleable.TkpdHintTextInputLayout_hintEnabled), anyBoolean()))
//                .thenReturn(true);
//    }
//}
