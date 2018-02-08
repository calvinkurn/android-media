import React, { Component } from 'react'
import { Modal, View, StyleSheet, FlatList, TouchableNativeFeedback } from 'react-native'
import { Text } from '../../../common/TKPText'

class EtalaseSelectPopUp extends Component {
  constructor(props) {
    super(props)
  }

  renderItem = ({ item }) => {
    return (
      <TouchableNativeFeedback onPress={() => {
        this.props.onChange(item.id)
        this.props.onBackTap()
      }}>
        <View>
          {
            item.id == this.props.value ?
              <Text style={styles.listHeaderText}>{item.name}</Text> :
              <Text style={styles.itemText}>{item.name}</Text>
          }
        </View>
      </TouchableNativeFeedback>
    )
  }

  render() {
    const visible = this.props.visible
    const onBackTap = this.props.onBackTap
    const options = this.props.options
    const value = this.props.value
    const selectedEtalase = this.props.options.map(o => o.id == value)
    return (
      < Modal
        animationType='none'
        transparent={true}
        hardwareAccelerated={true}
        visible={visible}
        onRequestClose={onBackTap} >
        <View style={styles.container}>
          <View style={styles.overlay}>
            <View style={styles.box}>
              <View style={styles.header}>
                <Text style={styles.headerText}>Pilih Etalase</Text>
              </View>
              <FlatList
                keyExtractor={item => item.id}
                renderItem={this.renderItem}
                data={options} />
            </View>
          </View>
        </View>
      </Modal >
    )
  }
}

export default EtalaseSelectPopUp

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: 'rgba(0,0,0,0.5)',
  },
  overlay: {
    flex: 1,
    position: 'absolute',
    left: 0,
    top: 0,
    width: '100%',
    height: '100%',
    justifyContent: 'center',
    alignItems: 'center',
  },
  box: {
    top: 0,
    backgroundColor: '#fff',
    width: '85%',
    borderRadius: 9,
    height: '40%',
    flexDirection: 'column',
  },
  header: {
    borderBottomWidth: 2,
    borderBottomColor: '#e0e0e0',
  },
  itemText: {
    paddingLeft: 30,
    paddingVertical: 20,
    fontSize: 14,
  },
  headerText: {
    fontSize: 16,
    paddingLeft: 30,
    paddingVertical: 20,
    color: 'black',
    fontFamily: 'Roboto-Medium'
  },
  listHeader: {

  },
  listHeaderText: {
    paddingLeft: 30,
    paddingVertical: 20,
    fontSize: 14,
    color: '#42B549',
    fontFamily: 'Roboto-Medium'
  }
})