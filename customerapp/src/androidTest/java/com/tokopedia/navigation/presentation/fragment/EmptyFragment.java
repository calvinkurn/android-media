package com.tokopedia.navigation.presentation.fragment;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tokopedia.tkpd.test.R;

public class EmptyFragment extends Fragment {

    TextView emptyTextView;

    public static Fragment newInstance(int number) {
        Bundle arguments = new Bundle();
        arguments.putInt("NUMBER", number);
        Fragment fragment = new EmptyFragment();
        fragment.setArguments(arguments);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.empty_layout_fragment, null);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        emptyTextView = view.findViewById(R.id.empty_textView);

        if (savedInstanceState == null && getArguments() != null) {
            int number = 0;
            if ((number = getArguments().getInt("NUMBER", -1)) != -1) {
                emptyTextView.setText(Integer.toString(number));
            }
        }
        super.onViewCreated(view, savedInstanceState);
    }
}
