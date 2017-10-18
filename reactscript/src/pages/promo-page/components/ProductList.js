import React from 'react'
import { View, StyleSheet } from 'react-native'
import PropTypes from 'prop-types'
import Product from './Product'

const styles = StyleSheet.create({
  container: {
    flex: 1,
    flexDirection: 'row',
    flexWrap: 'wrap',
  },
})

const ProductList = ({ products }) => (
  <View style={styles.container}>
    {
      products.map(p => (
        <Product product={p} key={p.data.id} />
      ))
    }
  </View>
)

ProductList.propTypes = {
  products: PropTypes.arrayOf.isRequired,
}

export default ProductList
