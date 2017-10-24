import React from 'react'
import { View, Text } from 'react-native'
import PropTypes from 'prop-types'
import ProductList from './ProductList'
import TKPButton from '../common/TKPPrimaryBtn'
import { NavigationModule } from 'NativeModules'



const Category = ({ category, products, dataSlug, applink }) => {
  const total_products = products.length > 12 ? (products.length - 1) : products.length
  const products_items = products.length > 12 ? (products.slice(0, 12)) : products
  return (
    <View>
      <View style={{ marginBottom: 10 }}>
        <View style={{ backgroundColor: '#FFF', marginVertical: 10 }}>
          <View style={{ paddingVertical: 15, paddingHorizontal: 10, borderBottomWidth:1, borderTopWidth: 1, borderColor: '#E0E0E0' }}>
            <Text numberOfLines={1} style={{ fontSize: 16, fontWeight: '600', color: 'rgba(0,0,0,.7)' }}>{category.category_name}</Text>
          </View>
          <View style={{ flex: 1 }}>
            <ProductList products={products_items} />
          </View>
        </View>
      </View>
      {
        products.length > 12 && (
          <View style={{ flex: 1, justifyContent: 'center', alignItems: 'center' }}>
            <TKPButton content="View All" onTap={() => {
              NavigationModule.navigateWithMobileUrl('', `${applink.items}/${dataSlug.slug}/${category.slug}?override_url=1`, "")}}
              type="small" />
          </View>
        )
      }
    </View>
  )
}


Category.propTypes = {
  category: PropTypes.shape({
    category_name: PropTypes.string.isRequired,
  }).isRequired,
}

export default Category