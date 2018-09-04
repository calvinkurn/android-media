package com.tokopedia.mitra.digitalcategory.presentation.fragment;

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
import com.tokopedia.common_digital.product.presentation.model.RenderOperatorModel;
import com.tokopedia.mitra.R;
import com.tokopedia.mitra.digitalcategory.di.AgentDigitalCategoryComponent;
import com.tokopedia.mitra.digitalcategory.di.DaggerAgentDigitalCategoryComponent;
import com.tokopedia.mitra.digitalcategory.presentation.compoundview.MitraDigitalCategoryView;
import com.tokopedia.mitra.digitalcategory.presentation.presenter.AgentDigitalCategoryContract;
import com.tokopedia.mitra.digitalcategory.presentation.presenter.MitraDigitalCategoryPresenter;

import javax.inject.Inject;

/**
 * Created by Rizky on 30/08/18.
 */
public class MitraDigitalCategoryFragment extends BaseDaggerFragment implements AgentDigitalCategoryContract.View {

    private MitraDigitalCategoryView mitraDigitalCategoryView;

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

        presenter.getCategory(1);
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
    public void renderWidgetView(RenderOperatorModel renderOperatorModel) {
        mitraDigitalCategoryView.renderWidgetViews(renderOperatorModel);
    }

}
