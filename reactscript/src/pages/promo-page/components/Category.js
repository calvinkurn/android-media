import React from 'react'
import { View, Text } from 'react-native'
import PropTypes from 'prop-types'
import ProductList from './ProductList'
import TKPButton from '../common/TKPPrimaryBtn'
import { NavigationModule } from 'NativeModules'



const Category = ({ category, products, dataSlug, applink }) => (
  <View style={{ marginBottom: 10 }}>
    <View style={{ backgroundColor: '#FFF', marginVertical: 10 }}>
      <View style={{ paddingVertical: 15, paddingHorizontal: 10, borderBottomWidth:1, borderTopWidth: 1, borderColor: '#E0E0E0' }}>
        <Text numberOfLines={1} style={{ fontSize: 16, fontWeight: '600', color: 'rgba(0,0,0,.7)' }}>{category.category_name}</Text>
      </View>
      <View style={{ flex: 1 }}>
        <ProductList products={category.products} />
      </View>
    </View>

    <View style={{ flex: 1, justifyContent: 'center', alignItems: 'center' }}>
      {products.length > 12 &&
        <TKPButton content="View All" onTap={() => { 
          NavigationModule.navigateWithMobileUrl('', `${applink.items}/${dataSlug.slug}/${category.slug}?override_url=1`, "")}}
          type="small" />
      }
    </View>
  </View>
)


Category.propTypes = {
  category: PropTypes.shape({
    category_name: PropTypes.string.isRequired,
  }).isRequired,
}

export default Category