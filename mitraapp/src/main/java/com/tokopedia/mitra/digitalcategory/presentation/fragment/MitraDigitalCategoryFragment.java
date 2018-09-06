package com.tokopedia.mitra.digitalcategory.presentation.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tokopedia.abstraction.base.app.BaseMainApplication;
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.common_digital.common.di.DaggerDigitalComponent;
import com.tokopedia.common_digital.common.di.DigitalComponent;
import com.tokopedia.common_digital.product.presentation.model.InputFieldModel;
import com.tokopedia.common_digital.product.presentation.model.Operator;
import com.tokopedia.mitra.R;
import com.tokopedia.mitra.digitalcategory.di.AgentDigitalCategoryComponent;
import com.tokopedia.mitra.digitalcategory.di.DaggerAgentDigitalCategoryComponent;
import com.tokopedia.mitra.digitalcategory.presentation.activity.MitraDigitalChooserActivity;
import com.tokopedia.mitra.digitalcategory.presentation.compoundview.MitraDigitalCategoryView;
import com.tokopedia.mitra.digitalcategory.presentation.model.DigitalCategoryModel;
import com.tokopedia.mitra.digitalcategory.presentation.presenter.MitraDigitalCategoryContract;
import com.tokopedia.mitra.digitalcategory.presentation.presenter.MitraDigitalCategoryPresenter;

import javax.inject.Inject;

/**
 * Created by Rizky on 30/08/18.
 */
public class MitraDigitalCategoryFragment extends BaseDaggerFragment implements MitraDigitalCategoryContract.View {

    private MitraDigitalCategoryView mitraDigitalCategoryView;

    private DigitalCategoryModel digitalCategoryModel;

    @Inject
    MitraDigitalCategoryPresenter presenter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootview = inflater.inflate(R.layout.fragment_agent_digital_category, container, false);

        mitraDigitalCategoryView = rootview.findViewById(R.id.view_category);

        return rootview;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        presenter.attachView(this);

        presenter.getCategory(5);
    }

    @Override
    protected void initInjector() {
        DigitalComponent digitalComponent =
                DaggerDigitalComponent.builder().baseAppComponent((
                        (BaseMainApplication) getActivity().getApplication()).getBaseAppComponent())
                        .build();
        AgentDigitalCategoryComponent agentDigitalCategoryComponent =
                DaggerAgentDigitalCategoryComponent.builder().digitalComponent(digitalComponent)
                        .build();
        agentDigitalCategoryComponent.inject(this);
    }

    @Override
    protected String getScreenName() {
        return null;
    }

    @Override
    public void renderWidgetView(DigitalCategoryModel digitalCategoryModel, String defaultId) {
        this.digitalCategoryModel = digitalCategoryModel;

        mitraDigitalCategoryView.setActionListener(new MitraDigitalCategoryView.ActionListener() {
            @Override
            public void onClickDropdown(InputFieldModel inputFieldModel, String selectedItemId) {
                if (inputFieldModel.getName().equals("operator_id")) {
                    String titleChooser = inputFieldModel.getText() + " " + digitalCategoryModel.getOperatorLabel();
                    Intent intent = MitraDigitalChooserActivity.newInstanceOperatorChooser(getActivity(),
                            digitalCategoryModel.getId(), titleChooser, digitalCategoryModel.getOperatorLabel(),
                            digitalCategoryModel.getName());
                    startActivityForResult(intent, 1001);
                } else if (inputFieldModel.getName().equals("product_id")) {
                    String titleChooser = inputFieldModel.getText();
                    Intent intent = MitraDigitalChooserActivity.newInstanceProductChooser(getActivity(),
                            digitalCategoryModel.getId(), "", titleChooser);
                    startActivityForResult(intent, 1001);
                }
            }
        });
        mitraDigitalCategoryView.renderWidgetViews(digitalCategoryModel.getRenderOperatorModel(), defaultId);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case 1001:
                if (resultCode == Activity.RESULT_OK && data != null)
                    handleCallbackOperatorChooser(
                            data.getParcelableExtra(
                                    MitraDigitalChooserActivity.EXTRA_CALLBACK_OPERATOR_DATA
                            )
                    );
                break;
        }
    }

    private void handleCallbackOperatorChooser(Operator operator) {
        mitraDigitalCategoryView.renderWidgetViews(digitalCategoryModel.getRenderOperatorModel(),
                operator.getOperatorId());
    }

}
