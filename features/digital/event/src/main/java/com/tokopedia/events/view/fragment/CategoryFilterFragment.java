package com.tokopedia.events.view.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatRadioButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.tokopedia.events.R;
import com.tokopedia.events.view.activity.EventFilterActivity;
import com.tokopedia.events.view.contractor.ICloseFragement;

import static com.tokopedia.events.view.contractor.EventFilterContract.CATEGORY_ID;

/**
 * Created by pranaymohapatra on 16/01/18.
 */

public class CategoryFilterFragment extends Fragment implements View.OnClickListener, AppCompatRadioButton.OnCheckedChangeListener {


    @Override
    public void onCheckedChanged(CompoundButton button, boolean selected) {

        if (selected) {
            if (currentToggled != null)
                currentToggled.setChecked(false);
            currentToggled = (RadioButton) button;
            int id = button.getId();
            if (id == R.id.rb_hiburan)
                selectedCategoryId = CATEGORY_ID[0];
            else if (id == R.id.rb_aktivitas)
                selectedCategoryId = CATEGORY_ID[1];
            else if (id == R.id.rb_musik)
                selectedCategoryId = CATEGORY_ID[2];
            else if (id == R.id.rb_olahraga)
                selectedCategoryId = CATEGORY_ID[3];
            else if (id == R.id.rb_teater)
                selectedCategoryId = CATEGORY_ID[4];
            else if (id == R.id.rb_seminar)
                selectedCategoryId = CATEGORY_ID[5];
            else if (id == R.id.rb_internasional)
                selectedCategoryId = CATEGORY_ID[6];
            else if (id == R.id.rb_opentrip)
                selectedCategoryId = CATEGORY_ID[7];
        }


    }

    public interface CategorySelectedListener {
        void setCategory(String selectedCategory);
    }


    private CategorySelectedListener listener;
    private ICloseFragement closeSelf;

    private static final String INDEX_CATEGORY = "index_category";
    AppCompatRadioButton rbMusik;
    AppCompatRadioButton rbSeminar;
    AppCompatRadioButton rbOlahraga;
    AppCompatRadioButton rbTeater;
    AppCompatRadioButton rbHiburan;
    AppCompatRadioButton rbAktivitas;
    AppCompatRadioButton rbInternasional;
    AppCompatRadioButton rbOpentrip;
    ImageView ivCloseFilter;
    TextView resetBtn;

    private RadioButton currentToggled;

    private int mCategory = -1;
    private String selectedCategoryId = "";


    public CategoryFilterFragment() {
        // Required empty public constructor
    }

    public static CategoryFilterFragment newInstance(int indexcategory) {
        CategoryFilterFragment fragment = new CategoryFilterFragment();
        Bundle args = new Bundle();
        args.putInt(INDEX_CATEGORY, indexcategory);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mCategory = getArguments().getInt(INDEX_CATEGORY);
        }
    }

    @SuppressLint("RestrictedApi")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View maincontent = inflater.inflate(R.layout.layout_fragment_filter_category, container, false);
        rbMusik = maincontent.findViewById(R.id.rb_musik);
        rbSeminar = maincontent.findViewById(R.id.rb_seminar);
        rbOlahraga = maincontent.findViewById(R.id.rb_olahraga);
        rbTeater = maincontent.findViewById(R.id.rb_teater);
        rbHiburan = maincontent.findViewById(R.id.rb_hiburan);
        rbAktivitas = maincontent.findViewById(R.id.rb_aktivitas);
        rbInternasional = maincontent.findViewById(R.id.rb_internasional);
        rbOpentrip = maincontent.findViewById(R.id.rb_opentrip);
        ivCloseFilter = maincontent.findViewById(R.id.iv_close_filter);
        resetBtn = maincontent.findViewById(R.id.tv_reset);
        ivCloseFilter.setOnClickListener(this);
        resetBtn.setOnClickListener(this);

        rbMusik.setSupportButtonTintList(
                ContextCompat.getColorStateList(getActivity(),
                        R.color.color_state_list_radio));
        rbSeminar.setSupportButtonTintList(
                ContextCompat.getColorStateList(getActivity(),
                        R.color.color_state_list_radio));
        rbOpentrip.setSupportButtonTintList(
                ContextCompat.getColorStateList(getActivity(),
                        R.color.color_state_list_radio));
        rbOlahraga.setSupportButtonTintList(
                ContextCompat.getColorStateList(getActivity(),
                        R.color.color_state_list_radio));
        rbInternasional.setSupportButtonTintList(
                ContextCompat.getColorStateList(getActivity(),
                        R.color.color_state_list_radio));
        rbHiburan.setSupportButtonTintList(
                ContextCompat.getColorStateList(getActivity(),
                        R.color.color_state_list_radio));
        rbTeater.setSupportButtonTintList(
                ContextCompat.getColorStateList(getActivity(),
                        R.color.color_state_list_radio));
        rbAktivitas.setSupportButtonTintList(
                ContextCompat.getColorStateList(getActivity(),
                        R.color.color_state_list_radio));

        switch (mCategory) {
            case 0:
                rbHiburan.setChecked(true);
                currentToggled = rbHiburan;
                selectedCategoryId = CATEGORY_ID[mCategory];
                break;
            case 1:
                rbAktivitas.setChecked(true);
                currentToggled = rbAktivitas;
                selectedCategoryId = CATEGORY_ID[mCategory];
                break;
            case 2:
                rbMusik.setChecked(true);
                currentToggled = rbMusik;
                selectedCategoryId = CATEGORY_ID[mCategory];
                break;
            case 3:
                rbOlahraga.setChecked(true);
                currentToggled = rbOlahraga;
                selectedCategoryId = CATEGORY_ID[mCategory];
                break;
            case 4:
                rbTeater.setChecked(true);
                currentToggled = rbTeater;
                selectedCategoryId = CATEGORY_ID[mCategory];
                break;
            case 5:
                rbSeminar.setChecked(true);
                currentToggled = rbSeminar;
                selectedCategoryId = CATEGORY_ID[mCategory];
                break;
            case 6:
                rbInternasional.setChecked(true);
                currentToggled = rbInternasional;
                selectedCategoryId = CATEGORY_ID[mCategory];
                break;
            case 7:
                rbOpentrip.setChecked(true);
                currentToggled = rbOpentrip;
                selectedCategoryId = CATEGORY_ID[mCategory];
                break;
            default:

        }
        return maincontent;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        listener = (EventFilterActivity) context;
        closeSelf = (EventFilterActivity) context;

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.iv_close_filter) {
            closeSelf.closeFragmentSelf();
        } else if (id == R.id.tv_reset) {
            selectedCategoryId = "";
            listener.setCategory(selectedCategoryId);
        } else if (id == R.id.tv_simpan) {
            listener.setCategory(selectedCategoryId);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
