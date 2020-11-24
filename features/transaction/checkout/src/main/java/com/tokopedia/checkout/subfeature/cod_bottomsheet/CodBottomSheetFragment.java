package com.tokopedia.checkout.subfeature.cod_bottomsheet;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import android.text.Editable;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.tokopedia.checkout.R;
import com.tokopedia.logisticCommon.data.analytics.CodAnalytics;

import org.xml.sax.XMLReader;

/**
 * Created by fajarnuha on 06/12/18.
 */
public class CodBottomSheetFragment extends BottomSheetDialogFragment {

    private String mTitle;
    private String mMessage;
    private static final String ARGUMENT_MESSAGE_HTML = "ARGUMENT_MESSAGE_HTML";
    private CodAnalytics mTracker;

    public CodBottomSheetFragment() {
    }

    public static CodBottomSheetFragment newInstance(String html) {
        Bundle bundle = new Bundle();
        bundle.putString(ARGUMENT_MESSAGE_HTML, html);
        CodBottomSheetFragment fragment = new CodBottomSheetFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getActivity() != null) {
            mTracker = new CodAnalytics();
            mTitle = getActivity().getString(com.tokopedia.purchase_platform.common.R.string.label_cod);
            if (getArguments() != null) {
                mMessage = getArguments().getString(ARGUMENT_MESSAGE_HTML);
            }
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(com.tokopedia.design.R.layout.widget_bottomsheet, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        FrameLayout container = view.findViewById(com.tokopedia.design.R.id.bottomsheet_container);
        View.inflate(getContext(), R.layout.bottom_sheet_cod_notification, container);

        TextView textViewTitle = view.findViewById(com.tokopedia.design.R.id.tv_title);
        textViewTitle.setText(mTitle);
        TextView textViewContent = view.findViewById(R.id.text_view_content);
        textViewContent.setText(Html.fromHtml(mMessage, null, new UlTagHandler()));

        view.findViewById(R.id.button_bottom_sheet_cod).setOnClickListener(view1 -> {
            if (mTracker != null) mTracker.eventClickMengertiIneligible();
            dismiss();
        });

        View layoutTitle = view.findViewById(com.tokopedia.design.R.id.layout_title);
        layoutTitle.setOnClickListener(view1 -> {
            if (mTracker != null) mTracker.eventClickXIneligible();
            dismiss();
        });

        if (mTracker != null) mTracker.eventViewErrorIneligible();
    }

    /*
    // This class handles <li> and <ul> tag from server response to be displayed correctly
     */
    private class UlTagHandler implements Html.TagHandler{
        @Override
        public void handleTag(boolean opening, String tag, Editable output,
                              XMLReader xmlReader) {
            if(tag.equals("ul") && !opening) output.append("\n");
            if(tag.equals("li") && opening) output.append("\n\n\t•");
        }
    }
}
