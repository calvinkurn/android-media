package com.tokopedia.loginregister.forbidden;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.tokopedia.abstraction.base.view.fragment.TkpdBaseV4Fragment;
import com.tokopedia.abstraction.common.utils.view.MethodChecker;
import com.tokopedia.applink.RouteManager;
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal;
import com.tokopedia.loginregister.R;
import com.tokopedia.unifycomponents.UnifyButton;
import com.tokopedia.url.TokopediaUrl;

/**
 * Created by meyta on 2/22/18.
 */

public class ForbiddenFragment extends TkpdBaseV4Fragment {

    private String URL = TokopediaUrl.getInstance().getWEB() + "terms?lang=id";
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
        return inflater.inflate(R.layout.fragment_forbidden_session, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        TextView title = view.findViewById(R.id.tv_title);
        TextView desc = view.findViewById(R.id.tv_message);
        UnifyButton btnRetry = view.findViewById(R.id.btn_retry);

        title.setText(MethodChecker.fromHtml(getString(R.string.forbidden_title)));
        desc.setText(MethodChecker.fromHtml(getString(R.string.forbidden_msg)));

        desc.setOnClickListener(v ->
                        RouteManager.route(getActivity(), ApplinkConstInternalGlobal.WEBVIEW, URL));

        btnRetry.setOnClickListener(v -> getActivity().finish());

    }

    @Override
    protected String getScreenName() {
        return FORBIDDEN_PAGE;
    }
}
