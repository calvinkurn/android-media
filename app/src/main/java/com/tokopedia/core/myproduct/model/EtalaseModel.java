package com.tokopedia.core.myproduct.model;

import org.parceler.Parcel;

import java.util.ArrayList;

/**
 * Created by m.normansyah on 10/12/2015.
 */
@Parcel(parcelsIndex = false)
public class EtalaseModel {
    String text;
    ArrayList<String> childs;
    ArrayList<GetEtalaseModel.EtalaseModel> childsComplete;

    /**
     * This is for parcelable
     */
    public EtalaseModel(){
        childs = new ArrayList<>();
        childsComplete = new ArrayList<>();
    }

    public EtalaseModel add(GetEtalaseModel.EtalaseModel childComplete){
        childsComplete.add(childComplete);
        childs.add(childComplete.getEtalase_name());
        return this;
    }

    public EtalaseModel add(String child){
        childs.add(child);
        return this;
    }

    public ArrayList<String> getChilds() {
        return childs;
    }

    public void setChilds(ArrayList<String> childs) {
        this.childs = childs;
    }

    public String getText() {
        return text;
    }

    public EtalaseModel setText(String temp) {
        this.text = temp;
        return this;
    }

    public static TextDeleteModel toTextDeleteModel(EtalaseModel model, int level){
        TextDeleteModel deleteModel = new TextDeleteModel(model.getText());
        deleteModel.setDataPosition(level);
        return deleteModel;
    }

    public static SimpleTextModel toSimpleTextModel(EtalaseModel model, int position, int level){
        SimpleTextModel simpleTextModel = new SimpleTextModel(model.getText());
        simpleTextModel.setLevel(level);
        simpleTextModel.setPosition(position);
        return simpleTextModel;
    }
}
