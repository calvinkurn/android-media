package com.tokopedia.checkout.view.feature.bottomsheetcod;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.tokopedia.checkout.R;

/**
 * Created by fajarnuha on 06/12/18.
 */
public class CodBottomSheetFragment extends BottomSheetDialogFragment {

    private String mTitle;

    public CodBottomSheetFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getActivity() != null) {
            mTitle = getActivity().getString(R.string.label_cod);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.widget_bottomsheet, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        FrameLayout container = view.findViewById(R.id.bottomsheet_container);
        View.inflate(getContext(), R.layout.bottom_sheet_cod_notification, container);

        TextView textViewTitle = view.findViewById(com.tokopedia.design.R.id.tv_title);
        textViewTitle.setText(mTitle);

        view.findViewById(R.id.button_bottom_sheet_cod).setOnClickListener(this::onCloseButtonClick);

        View layoutTitle = view.findViewById(com.tokopedia.design.R.id.layout_title);
        layoutTitle.setOnClickListener(this::onCloseButtonClick);
    }

    private void onCloseButtonClick(View view) {
        dismiss();
    }
}
