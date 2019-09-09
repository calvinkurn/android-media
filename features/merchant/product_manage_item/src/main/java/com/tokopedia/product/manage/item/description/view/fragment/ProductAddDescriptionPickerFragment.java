package com.tokopedia.product.manage.item.description.view.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.tokopedia.abstraction.base.view.fragment.TkpdBaseV4Fragment;
import com.tokopedia.core.util.MethodChecker;
import com.tokopedia.product.manage.item.R;
import com.tokopedia.product.manage.item.description.view.activity.ProductAddDescriptionInfoActivity;

/**
 * Created by nathan on 3/6/18.
 */

public class ProductAddDescriptionPickerFragment extends TkpdBaseV4Fragment {

    public static final String PRODUCT_DESCRIPTION = "PRODUCT_DESCRIPTION";

    public static ProductAddDescriptionPickerFragment newInstance(String description) {
        Bundle args = new Bundle();
        ProductAddDescriptionPickerFragment fragment = new ProductAddDescriptionPickerFragment();
        args.putString(PRODUCT_DESCRIPTION, description);
        fragment.setArguments(args);
        return fragment;
    }

    private EditText descriptionEditText;
    private View tipsView;

    private String description;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        description = bundle.getString(PRODUCT_DESCRIPTION);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_product_description, container, false);
        TextView tvTitleTips = view.findViewById(R.id.tv_title_tips);
        tvTitleTips.setText(MethodChecker.fromHtml(getString(R.string.product_description_tips_title)));
        tipsView = view.findViewById(R.id.linear_layout_tips);
        tipsView.setOnClickListener(view12 -> startActivity(new Intent(getActivity(), ProductAddDescriptionInfoActivity.class)));

        descriptionEditText = view.findViewById(R.id.edit_text_description);
        TextView texViewMenu = getActivity().findViewById(R.id.texViewMenu);
        texViewMenu.setText(getString(R.string.label_save));
        texViewMenu.setOnClickListener(view1 -> {
            Intent intent = new Intent();
            intent.putExtra(PRODUCT_DESCRIPTION, getDescriptionText());
            getActivity().setResult(Activity.RESULT_OK, intent);
            getActivity().finish();
        });

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (!TextUtils.isEmpty(description)) {
            descriptionEditText.setText(MethodChecker.fromHtmlPreserveLineBreak(description));
        }
    }

    public String getDescriptionText() {
        return descriptionEditText.getText().toString();
    }

    @Override
    protected String getScreenName() {
        return null;
    }
}
