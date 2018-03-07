package com.tokopedia.anals.type;

import com.apollographql.apollo.api.ScalarType;
import java.lang.Class;
import java.lang.Override;
import java.lang.String;
import javax.annotation.Generated;

@Generated("Apollo GraphQL")
public enum CustomType implements ScalarType {
  EMAIL {
    @Override
    public String typeName() {
      return "Email";
    }

    @Override
    public Class javaType() {
      return Object.class;
    }
  }
}
