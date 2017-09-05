import React from 'react'
import {
  View,
  Modal,
  TouchableWithoutFeedback,
  Image,
  Text,
  TextInput,
  SectionList,
  StyleSheet,
} from 'react-native'
import SearchNotFound from './SearchNotFound'

const renderItem = ({ item }) => (
  <View style={{
    height: 80, borderBottomWidth: 1,
    borderBottomColor: '#e0e0e0',
    paddingLeft: '10%',
    justifyContent: 'center'
  }}>
    <Text style={{ fontSize: 20 }}>{item.text}</Text>
  </View>
)

const renderHeader = ({ section }) => (
  <View style={{
    paddingLeft: '10%',
    justifyContent: 'center',
    height: 78,
    borderBottomWidth: 1,
    borderBottomColor: '#e0e0e0'
  }}>
    <Text style={{
      fontSize: 18,
      fontWeight: '400',
      color: 'rgba(0,0,0,.38)'
    }}>{section.title}</Text>
  </View>
)

const SearchList = ({ searchItems }) => {
  return (
    <View>
      <SectionList
        keyExtractor={(item) => {
          return item.id
        }}
        renderItem={renderItem}
        renderSectionHeader={renderHeader}
        sections={[ // homogenous rendering between sections
          { data: searchItems, title: 'Pencarian Terakhir' }
        ]}
      />
    </View>
  )
}

const SearchScreen = ({ visible, onBackPress, onSearch, items, onClearSearch, queryText, onSearchType }) => {
  return (
    <Modal
      animationType={'slide'}
      transparent={false}
      hardwareAccelerated={true}
      visible={visible}
      onRequestClose={onBackPress}>
      <View style={styles.container}>
        <View style={styles.searchTextBoxWrapper}>
          <TextInput
            style={styles.searchTextBox}
            placeholder='Cari Produk'
            autoFocus={true}
            inlineImageLeft='search_icon'
            inlineImagePadding={20}
            value={queryText}
            onChangeText={
              (text) => {
                onSearchType(text)
                onSearch(text)
              }
            }
          />
        </View>
        <View style={styles.closeIconWrapper}>
          <TouchableWithoutFeedback onPress={onClearSearch}>
            <Image source={require('./img/close-icon.png')} />
          </TouchableWithoutFeedback>
        </View>
      </View>
      {(items.length === 0 && queryText) ? <SearchNotFound /> : <SearchList searchItems={items} />}
      {/* <SearchNotFound /> */}
      <SearchList />
    </Modal>
  )
}

const styles = StyleSheet.create({
  container: {
    height: 80,
    backgroundColor: '#fff',
    flexDirection: 'row',
    alignItems: 'center',
    justifyContent: 'space-around',
    borderBottomWidth: 1,
    borderBottomColor: '#e0e0e0'
  },
  searchTextBoxWrapper: {
    width: '90%',
    paddingLeft: '6%',
    backgroundColor: '#fff',
  },
  searchTextBox: {
    height: 50,
    borderColor: '#fff',
    borderWidth: 1,
    fontSize: 18,
  },
  closeIconWrapper: {
    width: '10%',
  }
})
export default SearchScreen