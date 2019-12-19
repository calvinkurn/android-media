package com.tokopedia.sessioncommon.view.forbidden.fragment;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.tokopedia.abstraction.base.view.fragment.TkpdBaseV4Fragment;
import com.tokopedia.abstraction.common.utils.view.MethodChecker;
import com.tokopedia.sessioncommon.R;
import com.tokopedia.sessioncommon.view.forbidden.activity.ServiceActivity;

/**
 * Created by meyta on 2/22/18.
 */

public class ForbiddenFragment extends TkpdBaseV4Fragment {

    private String URL = "https://www.tokopedia.com/terms.pl#responsibility";
    private String FORBIDDEN_PAGE = "Forbidden Page";

    public static ForbiddenFragment createInstance() {
        ForbiddenFragment fragment = new ForbiddenFragment();
        Bundle bundle = new Bundle();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_forbidden, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        TextView title = view.findViewById(R.id.tv_title);
        TextView desc = view.findViewById(R.id.tv_message);
        Button btnRetry = view.findViewById(R.id.btn_retry);

        title.setText(MethodChecker.fromHtml(getString(R.string.forbidden_title)));
        desc.setText(MethodChecker.fromHtml(getString(R.string.forbidden_msg)));

        desc.setOnClickListener(v -> ServiceActivity.startActivity(getActivity(), URL));

        btnRetry.setText(R.string.forbidden_button);
        btnRetry.setOnClickListener(v -> getActivity().finish());

    }

    @Override
    protected String getScreenName() {
        return FORBIDDEN_PAGE;
    }
}
