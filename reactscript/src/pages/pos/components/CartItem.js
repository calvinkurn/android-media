import React, { Component } from 'react'
import { Text, View, StyleSheet, Image, TouchableWithoutFeedback } from 'react-native'
import PopUp from '../common/TKPPopupModal'

class CartItem extends Component {
  constructor(props) {
    super(props)
    this.state = { show: false }
  }

  togglePopUp = (visible) => {
    this.setState({ show: visible })
  }

  removeItem = (id) => {
    this.togglePopUp(false)
    this.props.onRemove(id)
  }

  render() {
    const item = this.props.item
    const onIncr = this.props.onIncr
    const onDecr = this.props.onDecr
    const onRemove = this.props.onRemove

    return (
      <View style={styles.container}>
        <PopUp
          visible={this.state.show}
          animationType='fade'
          onBackPress={() => { this.togglePopUp(false) }}
          title='Konfirmasi Pembatalan'
          subTitle={`${item.name} - senilai ${item.price}`}
          onSecondOptionTap={() => { this.removeItem(item.id) }}
          onFirstOptionTap={() => { this.togglePopUp(false) }}
          firstOptionText='Tidak'
          secondOptionText='Ya'
          onCloseIconTap={() => { this.togglePopUp(false) }}
        />
        <View style={{ width: '8%' }}>
          <TouchableWithoutFeedback
            onPress={() => { this.togglePopUp(true) }} >
            <Image source={require('./img/trash.png')} style={{ width: 20, height: 20, resizeMode: 'contain' }} />
          </TouchableWithoutFeedback>
        </View>
        <View style={{ width: '20%' }}>
          <Image source={{ uri: item.imageUrl }} style={styles.imageStyle} />
        </View>
        <View style={{ width: '40%' }}>
          <Text ellipsizeMode='tail' numberOfLines={2}>{item.name}</Text>
          <Text style={styles.price}>Rp. {item.price}</Text>
        </View>
        <View style={styles.qtyContainer}>
          <Text>Qty</Text>
          <View style={styles.qtyControlContainer}>
            <TouchableWithoutFeedback
              disabled={item.qty === 1}
              onPress={() => { onDecr(item.id) }}>
              <View>
                <Image source={require('./img/btn_minus.png')}></Image>
              </View>
            </TouchableWithoutFeedback>
            <View style={styles.qty}>
              <Text style={{ fontSize: 16 }}> {item.qty} </Text>
            </View>
            <TouchableWithoutFeedback
              onPress={() => { onIncr(item.id) }}>
              <View>
                <Image source={require('./img/btn_plus.png')}></Image>
              </View>
            </TouchableWithoutFeedback>
          </View>
        </View>
      </View>
    )
  }
}

const styles = StyleSheet.create({
  container: {
    flexDirection: 'row',
    backgroundColor: '#fff',
    borderBottomColor: '#e0e0e0',
    borderBottomWidth: 1,
    paddingHorizontal: 5,
    paddingVertical: 10,
    alignItems: 'center',
    justifyContent: 'space-between',
  },
  imageStyle: {
    width: 60,
    height: 60,
  },
  price: {
    color: '#ff5722',
    fontSize: 13,
    fontWeight: '600',
    lineHeight: 20,
  },
  qtyControlContainer: {
    flexDirection: 'row',
    alignItems: 'center',
  },
  qty: {
    paddingVertical: 4,
    paddingHorizontal: 8,
    borderColor: '#e0e0e0',
    borderTopWidth: 1,
    borderBottomWidth: 1,
  },
  qtyContainer: {
    width: '22%',
    flexDirection: 'column',
    alignItems: 'center',
    justifyContent: 'space-between',
    paddingRight: 10
  }

})

export default CartItem
