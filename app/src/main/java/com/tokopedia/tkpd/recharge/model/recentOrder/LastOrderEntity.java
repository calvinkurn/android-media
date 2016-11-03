package com.tokopedia.tkpd.recharge.model.recentOrder;

/**
 * @author Kulomady on 8/26/2016.
 */
public class LastOrderEntity {
    private String type;
    private int id;

    private AttributesBean attributes;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public AttributesBean getAttributes() {
        return attributes;
    }

    public void setAttributes(AttributesBean attributes) {
        this.attributes = attributes;
    }

    public static class AttributesBean {
        private String client_number;
        private int product_id;
        private int operator_id;
        private int category_id;

        public String getClient_number() {
            return client_number;
        }

        public void setClient_number(String client_number) {
            this.client_number = client_number;
        }

        public int getProduct_id() {
            return product_id;
        }

        public void setProduct_id(int product_id) {
            this.product_id = product_id;
        }

        public int getOperator_id() {
            return operator_id;
        }

        public void setOperator_id(int operator_id) {
            this.operator_id = operator_id;
        }

        public int getCategory_id() {
            return category_id;
        }

        public void setCategory_id(int category_id) {
            this.category_id = category_id;
        }
    }
}
