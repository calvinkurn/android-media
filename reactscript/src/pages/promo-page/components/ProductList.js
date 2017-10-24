import React from 'react'
import { View, StyleSheet } from 'react-native'
import PropTypes from 'prop-types'
import Product from './Product'

const styles = StyleSheet.create({
  container: {
    height: '100%',
  },
  productsRow: {
    flex: 1,
    flexDirection: 'row',
    borderColor: '#E0E0E0',
    borderBottomWidth: 1,
  },
})

const ProductList = ({ products }) => (
  <View style={styles.container}>
    {
      products.reduce((prev, curr, index) => (index % 2 === 0 ? prev.push([curr]) : prev[prev.length - 1].push(curr)) && prev, [])
        .map((pRow, i) => (
          <View style={styles.productsRow} key={i} >
            {
              pRow.map((p, index) => (
                <Product product={p} key={index} />
              ))
            }
          </View>
        ))
    }
  </View>
)

ProductList.propTypes = {
  products: PropTypes.arrayOf.isRequired,
}

export default ProductList
