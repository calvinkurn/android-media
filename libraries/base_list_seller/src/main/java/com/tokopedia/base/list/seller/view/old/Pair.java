package com.tokopedia.base.list.seller.view.old;

/**
 * This is just for this class
 * @param <E>
 * @param <F>
 */
@Deprecated
public class Pair<E,F>{
    E model1;
    F model2;

    public Pair(){
        this.model1 = null;
        this.model2 = null;
    }

    public Pair(E model1, F model2){
        this.model1 = model1;
        this.model2 = model2;
    }

    public E getModel1() {
        return model1;
    }

    public void setModel1(E model1) {
        this.model1 = model1;
    }

    public F getModel2() {
        return model2;
    }

    public void setModel2(F model2) {
        this.model2 = model2;
    }

    @Override
    public String toString() {
        return "Pair{" +
                "model1=" + model1 +
                ", model2=" + model2 +
                '}';
    }
}
